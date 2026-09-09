package com.hub.backend.models;

import java.util.List;

public class AddFacilityRequest {

    private SportsFacility facility;
    private List<Court> courts;

    //constructors
    public AddFacilityRequest() {}
    
    public AddFacilityRequest(SportsFacility facility, List<Court> courts) {
        this.facility = facility;
        this.courts = courts;
    }
    
    //getters and setters
    public SportsFacility getFacility() {
        return facility;
    }
    public void setFacility(SportsFacility facility) {
        this.facility = facility;
    }
    public List<Court> getCourts() {
        return courts;
    }
    public void setCourts(List<Court> courts) {
        this.courts = courts;
    }
    
}