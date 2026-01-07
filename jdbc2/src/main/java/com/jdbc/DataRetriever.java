package com.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private final DBConnection dbConnection;

    public DataRetriever() {
        this.dbConnection = new DBConnection();
    }

    public Team findTeamById(Integer id) {
        Team team = null;

        String teamQuery = "SELECT id, name, continent FROM team WHERE id = ?";
        String playersQuery = """
                    SELECT id, name, age, position, goal_nb
                    FROM player
                    WHERE team_id = ?
                """;

        try (Connection connection = dbConnection.getConnection()) {

            try (PreparedStatement teamStmt = connection.prepareStatement(teamQuery)) {
                teamStmt.setInt(1, id);

                ResultSet teamRs = teamStmt.executeQuery();

                if (teamRs.next()) {
                    team = new Team(
                            teamRs.getInt("id"),
                            teamRs.getString("name"),
                            ContinentEnum.valueOf(teamRs.getString("continent")));
                } else {
                    return null;
                }

            }


            try (PreparedStatement playerStmt = connection.prepareStatement(playersQuery)) {
                playerStmt.setInt(1, id);

                ResultSet playerRs = playerStmt.executeQuery();

                while (playerRs.next()) {
                    Player player = new Player(
                            playerRs.getInt("id"),
                            playerRs.getString("name"),
                            playerRs.getInt("age"),
                            PositionEnum.valueOf(playerRs.getString("position")),
                            team.getId(),
                            (Integer) playerRs.getObject("goal_nb") // nullable
                    );

                    team.getPlayers().add(player);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return team;
    }

    public List<Player> findPlayers(int page, int size) {
        List<Player> players = new ArrayList<>();
        int offset = (page - 1) * size;

        try {
            Connection conn = dbConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, name, age, position, team_id, goal_nb FROM player LIMIT ? OFFSET ?");
            ps.setInt(1, size);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                players.add(new Player(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        PositionEnum.valueOf(rs.getString("position").toUpperCase()),
                        rs.getInt("team_id"),
                        (Integer) rs.getObject("goal_nb")));
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return players;
    }

    public List<Player> createPlayers(List<Player> newPlayers) {
        String sql = """
                    INSERT INTO player (id, name, age, position, team_id, goal_nb)
                VALUES (?, ?, ?, ?::player_position, ?)
                    """;
        try (Connection connection = dbConnection.getConnection()) {

            connection.setAutoCommit(false);

            try (PreparedStatement insertStmt = connection.prepareStatement(sql)) {

                for (Player player : newPlayers) {

                    if (playerExists(connection, player.getId())) {
                        throw new RuntimeException(
                                "The player with id " + player.getId() + " already exists");
                    }

                    insertStmt.setInt(1, player.getId());
                    insertStmt.setString(2, player.getName());
                    insertStmt.setInt(3, player.getAge());
                    insertStmt.setString(4, player.getPosition().name());
                    insertStmt.setInt(5, player.getTeamId());
                    insertStmt.setObject(6, player.getGoalNb());
                    insertStmt.executeUpdate();
                }

                connection.commit();
                return newPlayers;

            } catch (Exception e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Creation failed", e);
        }
    }

    private boolean playerExists(Connection connection, int playerId) throws SQLException {
        String sql = "SELECT 1 FROM player WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public Team saveTeam(Team team) {
        String verifySql = "SELECT 1 FROM team WHERE id = ?";
        String insertSql = "INSERT INTO team (id, name, continent) VALUES (?, ?, ?)";
        String updateSql = "UPDATE team SET name = ?, continent = ? WHERE id = ?";

        try (Connection connection = dbConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                boolean exists;
                try (PreparedStatement ps = connection.prepareStatement(verifySql)) {
                    ps.setInt(1, team.getId());
                    exists = ps.executeQuery().next();
                }

                if (exists) {
                    try (PreparedStatement ps = connection.prepareStatement(updateSql)) {
                        ps.setString(1, team.getName());
                        ps.setString(2, team.getContinent().name());
                        ps.setInt(3, team.getId());
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                        ps.setInt(1, team.getId());
                        ps.setString(2, team.getName());
                        ps.setString(3, team.getContinent().name());
                        ps.executeUpdate();
                    }
                }

                List<Player> players = team.getPlayers();
                for (int i = 0; i < players.size(); i++) {
                    Player player = players.get(i);
                    try (PreparedStatement ps = connection.prepareStatement(
                            "UPDATE player SET team_Id = ? WHERE id = ?")) {
                        ps.setInt(1, team.getId());
                        ps.setInt(2, player.getId());
                        ps.executeUpdate();
                    }
                }

                connection.commit();
                return team;

            } catch (Exception e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de l'équipe", e);
        }
    }

    public List<Team> findTeamsByPlayerName(String playerName) {
        List<Team> teams = new ArrayList<>();

        String sql = """
                    SELECT DISTINCT t.id, t.name, t.ContinentEnum
                    FROM team t
                    JOIN player p ON t.id = p.team_Id
                    WHERE p.name LIKE ?
                """;

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + playerName + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Team team = new Team(
                            rs.getInt("id"),
                            rs.getString("name"),
                            ContinentEnum.valueOf(rs.getString("ContinentEnum")));
                    teams.add(team);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des équipes", e);
        }

        return teams;
    }

    public List<Player> findPlayersByCriteria(
            String playerName,
            PositionEnum position,
            String teamName,
            ContinentEnum continent,
            int page,
            int size) {

        List<Player> players = new ArrayList<>();
        int offset = (page - 1) * size;

        StringBuilder sql = new StringBuilder("""
                    SELECT p.id, p.name, p.age, p.position, p.team_id
                    FROM player p
                    JOIN team t ON p.team_id = t.id
                    WHERE 1=1
                """);

        if (playerName != null && !playerName.isEmpty()) {
            sql.append(" AND p.name LIKE ?");
        }
        if (position != null) {
            sql.append(" AND p.position = ?::player_position");
        }
        if (teamName != null && !teamName.isEmpty()) {
            sql.append(" AND t.name LIKE ?");
        }
        if (continent != null) {
            sql.append(" AND t.continent = ?::continents");
        }

        sql.append(" LIMIT ? OFFSET ?");

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            int index = 1;

            if (playerName != null && !playerName.isEmpty()) {
                ps.setString(index++, "%" + playerName + "%");
            }
            if (position != null) {
                ps.setString(index++, position.name());
            }
            if (teamName != null && !teamName.isEmpty()) {
                ps.setString(index++, "%" + teamName + "%");
            }
            if (continent != null) {
                ps.setString(index++, continent.name());
            }

            ps.setInt(index++, size);
            ps.setInt(index, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    players.add(new Player(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            PositionEnum.valueOf(rs.getString("position")),
                            rs.getInt("team_id"),
                            (Integer) rs.getObject("goal_nb")));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche paginée des joueurs", e);
        }

        return players;
    }

}
