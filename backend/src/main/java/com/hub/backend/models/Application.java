package com.hub.backend.models;

public class Application {
    
    private int id;
    private int reservationId;
    private int athleteId;
    private String status;

    //constructors
    public Application() {}

    public Application(int id, int reservationId, int athleteId, String status) {
        this.id = id;
        this.reservationId = reservationId;
        this.athleteId = athleteId;
        this.status = status;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getReservationId() {
        return reservationId;
    }
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    public int getAthleteId() {
        return athleteId;
    }
    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
}
