package com.jdbc;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private int id;
    private String name;
    private ContinentEnum continent;
    private List<Player> players;

    public Team(int id, String name, ContinentEnum continent) {
        this.id = id;
        this.name = name;
        this.continent = continent;
        this.players = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public ContinentEnum getContinent() {
        return continent;
    }
    public List<Player> getPlayers() {
        return players;
    }

    public Integer getPlayersCount() {
         throw new UnsupportedOperationException("Not implemented yet.");
    }
}
