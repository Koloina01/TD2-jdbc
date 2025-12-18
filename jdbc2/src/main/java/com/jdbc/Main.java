package com.jdbc;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        DataRetriever dataRetriever = new DataRetriever();

       
        List<Player> playersPage1 = dataRetriever.findPlayers(1, 2);

        for (Player p : playersPage1) {
            System.out.println("- " + p.getName());
            // Attendu : Thibaut Courtois, Dani Carvajal
        }


        List<Player> playersPage3 = dataRetriever.findPlayers(3, 5);
        System.out.println("Nombre de joueurs page 3: " + playersPage3.size());


 
        

        try {
            List<Player> playersWithDup = List.of(
                    new Player(6, "Jude Bellingham", 23, PositionEnum.STR, 0),
                    new Player(7, "Pedri", 24, PositionEnum.MIDF, 0)
            );

            dataRetriever.createPlayers(playersWithDup);

        } catch (RuntimeException e) {
            System.out.println("Exception levée comme attendu pour doublons");
        }

        
        List<Player> playersNew = List.of(
                new Player(6, "Vini", 25, PositionEnum.STR, 0),
                new Player(7, "Pedri", 24, PositionEnum.MIDF, 0)
        );

        List<Player> createdPlayers = dataRetriever.createPlayers(playersNew);

        for (Player p : createdPlayers) {
            System.out.println(p.getName());
            // Attendu : Vini, Pedri
        }


        

        Team teamToUpdate = dataRetriever.findTeamsByPlayerName("Real").get(0);

        Player newPlayer = new Player(6, "Vini", 25, PositionEnum.STR, teamToUpdate.getId());

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
            // Attendu : Real Madrid, Atletico Madrid
        }
        System.out.println("PWD = " + System.getProperty("user.dir"));

    }
}

