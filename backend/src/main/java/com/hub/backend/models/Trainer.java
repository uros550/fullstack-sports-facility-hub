package com.hub.backend.models;

public class Trainer {
    
    private int id;
    private String firstName;
    private String lastName;
    private String specialization;
    private float rating;
    private float pricePerHour;
    private int sportId;
    private String sportName;
    private int facilityId;
    private String facilityName;
    private boolean isActive;

    //constructors
    public Trainer() {}

    public Trainer(int id, String firstName, String lastName, String specialization, float rating, float pricePerHour,
            int sportId, String sportName, int facilityId, String facilityName, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.rating = rating;
        this.pricePerHour = pricePerHour;
        this.sportId = sportId;
        this.sportName = sportName;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.isActive = isActive;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public float getPricePerHour() {
        return pricePerHour;
    }
    public void setPricePerHour(float pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
    public int getSportId() {
        return sportId;
    }
    public void setSportId(int sportId) {
        this.sportId = sportId;
    }
    public String getSportName() {
        return sportName;
    }
    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
    public int getFacilityId() {
        return facilityId;
    }
    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }
    public String getFacilityName() {
        return facilityName;
    }
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }
    public boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

}
