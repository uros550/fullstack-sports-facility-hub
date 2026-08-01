package com.hub.backend.models;

import java.util.List;

public class SearchFacilitiesRequest {

    private String name;
    private List<String> cities;
    private Integer sportId;
    private String courtType;
    //logged athlete
    private boolean freeToday;

    //constructor
    public SearchFacilitiesRequest() {}

    //getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getCities() {
        return cities;
    }
    public void setCities(List<String> cities) {
        this.cities = cities;
    }
    public Integer getSportId() {
        return sportId;
    }
    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }
    public String getCourtType() {
        return courtType;
    }
    public void setCourtType(String courtType) {
        this.courtType = courtType;
    }
    public boolean isFreeToday() {
        return freeToday;
    }
    public void setFreeToday(boolean freeToday) {
        this.freeToday = freeToday;
    }
}