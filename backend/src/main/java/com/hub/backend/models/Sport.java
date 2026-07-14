package com.hub.backend.models;

public class Sport {

    private int id;
    private String name;
    private int requiredPlayers;

    //constructors
    public Sport() {}

    public Sport(int id, String name, int requiredPlayers) {
        this.id = id;
        this.name = name;
        this.requiredPlayers = requiredPlayers;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getRequiredPlayers() {
        return requiredPlayers;
    }
    public void setRequiredPlayers(int requiredPlayers) {
        this.requiredPlayers = requiredPlayers;
    }
    
}