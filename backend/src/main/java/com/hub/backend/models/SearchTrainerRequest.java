package com.hub.backend.models;

public class SearchTrainerRequest {
    
    private int facilityId;
    private int sportId;

    //constructors
    public SearchTrainerRequest() {}
    
    public SearchTrainerRequest(int facilityId, int sportId) {
        this.facilityId = facilityId;
        this.sportId = sportId;
    }
    
    //getters and setters
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
    
}
