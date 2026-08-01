package com.hub.backend.models;

public class AvailabilitySlot {

    private String startTime;
    private String endTime;
    private boolean available;

    //constructors
    public AvailabilitySlot() {}
    
    public AvailabilitySlot(String startTime, String endTime, boolean available) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = available;
    }

    //getters and setters
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
}