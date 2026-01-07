package com.jdbc;

public class Player {
    private int id;
    private String name;
    private int age;
    private PositionEnum position;
    private int teamId;
    private Integer goalNb;

    public Player(int id, String name, int age, PositionEnum position, int teamId, Integer goalNb) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.position = position;
        this.teamId = teamId;
        this.goalNb = goalNb;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public PositionEnum getPosition() {
        return position;
    }

    public int getTeamId() {
        return teamId;
    }
    public Integer getGoalNb() {
        return goalNb;
    }

    public String getTeamName() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int setId(int id) {
        this.id = id;
        return id;
    }
    public String setName(String name) {
        this.name = name;
        return name;
    }
    public int setAge(int age) {
        this.age = age;
        return age;
    }
    public PositionEnum setPosition(PositionEnum position) {
        this.position = position;
        return position;
    }
    public int setTeamId(int teamId) {
        this.teamId = teamId;
        return teamId;
    }
    public Integer setGoalNb(Integer goalNb) {
        this.goalNb = goalNb;
        return goalNb;
    }
    

    @Override

    public String toString() {
        return "Player{id=" + id + ", name='" + name + "', age=" + age + ", position=" + position + ", teamId=" + teamId + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return id == player.id && age == player.age && teamId == player.teamId &&
               name.equals(player.name) && position == player.position;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(id);
        result = 31 * result + name.hashCode();
        result = 31 * result + Integer.hashCode(age);
        result = 31 * result + position.hashCode();
        result = 31 * result + Integer.hashCode(teamId);
        return result;
    }
}
