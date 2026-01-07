package com.jdbc;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        DataRetriever dataRetriever = new DataRetriever();

        Team team = dataRetriever.findTeamById(1);

        if (team != null) {
            System.out.println(team.getName());
        } else {
            System.out.println("Équipe non trouvée");
        }

        List<Player> playersPage1 = dataRetriever.findPlayers(1, 2);

        for (Player p : playersPage1) {
            System.out.println("- " + p.getName());
            /* Attendu : Thibaut Courtois, Dani Carvajal */
        }

        List<Player> playersPage3 = dataRetriever.findPlayers(3, 5);
        System.out.println("Nombre de joueurs page 3: " + playersPage3.size());

        try {
            List<Player> playersWithDup = List.of(
                    new Player(6, "Jude Bellingham", 23, PositionEnum.STR, 0, 0),
                    new Player(7, "Pedri", 24, PositionEnum.MIDF, 0, 1));

            dataRetriever.createPlayers(playersWithDup);

        } catch (RuntimeException e) {
            System.out.println("Exception levée comme attendu pour doublons");
        }

        List<Player> playersNew = List.of(
                new Player(6, "Thibaut Courtois", 31, PositionEnum.GK, 1, 0),
                new Player(7, "Dani Carjaval", 32, PositionEnum.DEF, 1, 2),
                new Player(8, "Jude Bellingham", 23, PositionEnum.STR, 1, 5),
                new Player(9, "Robert Lewandowski", 34, PositionEnum.STR, 1, null),
                new Player(10, "Antoine Griezmann", 32, PositionEnum.STR, 1, null));

        List<Player> createdPlayers = dataRetriever.createPlayers(playersNew);
        for (Player p : createdPlayers) {
            System.out.println(p.getName());

        }

        Team teamToUpdate = dataRetriever.findTeamsByPlayerName("Real").get(0);

        Player newPlayer = new Player(6, "Vini", 25, PositionEnum.STR, teamToUpdate.getId(), null);

        teamToUpdate.getPlayers().add(newPlayer);

        dataRetriever.saveTeam(teamToUpdate);

        Team updatedTeam = dataRetriever.findTeamsByPlayerName("Real").get(0);

        System.out.println("Liste des joueurs après ajout:");
        for (Player p : updatedTeam.getPlayers()) {
            System.out.println("- " + p.getName());
        }

        List<Team> teamsWithAn = dataRetriever.findTeamsByPlayerName("an");

        for (Team t : teamsWithAn) {
            System.out.println("- " + t.getName());

        }
        System.out.println("PWD = " + System.getProperty("user.dir"));

        System.out.println("=== TEST findPlayersByCriteria ===");

        List<Player> result = dataRetriever.findPlayersByCriteria(
                "ud",
                PositionEnum.MIDF,
                "Madrid",
                ContinentEnum.EUROPA,
                1,
                10);

        for (Player p : result) {
            System.out.println("- " + p.getName());
        }/* Résultat attendu : Jude Bellingham */

        Team real = dataRetriever.findTeamById(1);
        try {
            System.out.println("Total buts : " + real.getPlayersGoals());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

    }
}
