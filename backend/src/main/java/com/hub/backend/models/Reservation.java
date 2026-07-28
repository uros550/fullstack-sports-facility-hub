package com.hub.backend.models;

import java.time.LocalDateTime;

public class Reservation {
    
    private int id;
    private int facilityId;
    private String facilityName;
    private String city;
    private int courtId;
    private String courtName;
    private int athleteId;
    private int sportId;
    private String sportName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private int missingPlayers;

    //constructors
    public Reservation() {}

    public Reservation(int id, int facilityId, String facilityName, String city, int courtId, String courtName, int athleteId, int sportId, String sportName, LocalDateTime startTime, LocalDateTime endTime, String status, int missingPlayers) {
        this.id = id;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.city = city;
        this.courtId = courtId;
        this.courtName = courtName;
        this.athleteId = athleteId;
        this.sportId = sportId;
        this.sportName = sportName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.missingPlayers = missingPlayers;
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
    public String getFacilityName() {
        return facilityName;
    }
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
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
    public int getAthleteId() {
        return athleteId;
    }
    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getMissingPlayers() {
        return missingPlayers;
    }
    public void setMissingPlayers(int missingPlayers) {
        this.missingPlayers = missingPlayers;
    }
}
