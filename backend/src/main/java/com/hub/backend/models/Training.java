package com.hub.backend.models;

import java.time.LocalDateTime;

public class Training {
    
    private int id;
    private int trainerId;
    private String trainerName;
    private int athleteId;
    private String athleteUsername;
    private int facilityId;
    private String facilityName;
    private int courtId;
    private String courtName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double price;
    private String status;
    
    //constructors  
    public Training() {}

    public Training(int id, int trainerId, String trainerName, int athleteId, String athleteUsername, int facilityId, String facilityName, int courtId, String courtName, LocalDateTime startTime, LocalDateTime endTime, double price, String status) {
        this.id = id;
        this.trainerId = trainerId;
        this.trainerName = trainerName;
        this.athleteId = athleteId;
        this.athleteUsername = athleteUsername;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.courtId = courtId;
        this.courtName = courtName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.status = status;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getTrainerId() {
        return trainerId;
    }
    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }
    public String getTrainerName() {
        return trainerName;
    }
    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
    public int getAthleteId() {
        return athleteId;
    }
    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }
    public String getAthleteUsername() {
        return athleteUsername;
    }
    public void setAthleteUsername(String athleteUsername) {
        this.athleteUsername = athleteUsername;
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
    public int getCourtId() {
        return courtId;
    }
    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }
    public String getCourtName() {
        return courtName;
    }
    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
  
}
