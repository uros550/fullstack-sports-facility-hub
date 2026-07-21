package com.hub.backend.models;

public class Court {
    
    private int id;
    private int facilityId;
    private int sportId;
    private String name;
    private String type;
    private int capacity;
    private String equipmentDescription;
    private double pricePerHour;
    private String sportName; 

    //constructors
    public Court() {}

    public Court(int id, int facilityId, int sportId, String name, String type, int capacity, String equipmentDescription, double pricePerHour, String sportName) {
        this.id = id;
        this.facilityId = facilityId;
        this.sportId = sportId;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.equipmentDescription = equipmentDescription;
        this.pricePerHour = pricePerHour;
        this.sportName = sportName;
    }

    //getters and setters 
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getFacilityId() {
        return facilityId;
    }
    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }
    public int getSportId() {
        return sportId;
    }
    public void setSportId(int sportId) {
        this.sportId = sportId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public String getEquipmentDescription() {
        return equipmentDescription;
    }
    public void setEquipmentDescription(String equipmentDescription) {
        this.equipmentDescription = equipmentDescription;
    }
    public double getPricePerHour() {
        return pricePerHour;
    }
    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
    public String getSportName() {
        return sportName;
    }
    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
}