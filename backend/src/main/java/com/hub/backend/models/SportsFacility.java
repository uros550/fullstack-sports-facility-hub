package com.hub.backend.models;

public class SportsFacility {

    private int id;
    private String name;
    private String address;
    private String city;
    private String mb;
    private String pib;
    private String description;
    private double latitude;
    private double longitude;
    private String workingHours;
    private double rating;
    private int maxPenalties;
    private String status;

    //constructors
    public SportsFacility() {}

    public SportsFacility(int id, String name, String address, String city, String mb, String pib, String description,
            double latitude, double longitude, String workingHours, double rating, int maxPenalties, String status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.mb = mb;
        this.pib = pib;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.workingHours = workingHours;
        this.rating = rating;
        this.maxPenalties = maxPenalties;
        this.status = status;
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
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getMb() {
        return mb;
    }
    public void setMb(String mb) {
        this.mb = mb;
    }
    public String getPib() {
        return pib;
    }
    public void setPib(String pib) {
        this.pib = pib;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getWorkingHours() {
        return workingHours;
    }
    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public int getMaxPenalties() {
        return maxPenalties;
    }
    public void setMaxPenalties(int maxPenalties) {
        this.maxPenalties = maxPenalties;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    
}
