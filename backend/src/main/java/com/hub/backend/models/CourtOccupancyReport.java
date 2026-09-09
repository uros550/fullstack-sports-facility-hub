package com.hub.backend.models;

public class CourtOccupancyReport {

    private String courtName;
    private int reservedHours;
    private double occupancyPercentage;

    //constructors
    public CourtOccupancyReport() {}

    public CourtOccupancyReport(String courtName, int reservedHours, double occupancyPercentage) {
        this.courtName = courtName;
        this.reservedHours = reservedHours;
        this.occupancyPercentage = occupancyPercentage;
    }

    //getters and setters
    public String getCourtName() {
        return courtName;
    }
    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }
    public int getReservedHours() {
        return reservedHours;
    }
    public void setReservedHours(int reservedHours) {
        this.reservedHours = reservedHours;
    }
    public double getOccupancyPercentage() {
        return occupancyPercentage;
    }
    public void setOccupancyPercentage(double occupancyPercentage) {
        this.occupancyPercentage = occupancyPercentage;
    }

}