package com.hub.backend.models;

import java.time.LocalDateTime;

public class TeammateReservation {

    private int reservationId;
    private int athleteId;
    private String athleteName;
    private int facilityId;
    private String facilityName;
    private int courtId;
    private String courtName;
    private int sportId;
    private String sportName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int missingPlayers;
    private String status;

    //constructors
    public TeammateReservation() {}

    public TeammateReservation(int reservationId, int athleteId, String athleteName, int facilityId, String facilityName, int courtId, String courtName, int sportId, String sportName, LocalDateTime startTime, LocalDateTime endTime, int missingPlayers, String status) {
        this.reservationId = reservationId;
        this.athleteId = athleteId;
        this.athleteName = athleteName;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.courtId = courtId;
        this.courtName = courtName;
        this.sportId = sportId;
        this.sportName = sportName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.missingPlayers = missingPlayers;
        this.status = status;
    }

    //getters and setters
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
    public String getAthleteName() {
        return athleteName;
    }
    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
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
    public int getMissingPlayers() {
        return missingPlayers;
    }
    public void setMissingPlayers(int missingPlayers) {
        this.missingPlayers = missingPlayers;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}