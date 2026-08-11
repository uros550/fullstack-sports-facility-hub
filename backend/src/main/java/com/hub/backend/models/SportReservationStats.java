package com.hub.backend.models;

public class SportReservationStats {
    
    private String sportName;
    private int reservedCount;
    private int playedCount;

    //constructors
    public SportReservationStats() {}
    
    public SportReservationStats(String sportName, int reservedCount, int playedCount) {
        this.sportName = sportName;
        this.reservedCount = reservedCount;
        this.playedCount = playedCount;
    }

    //getters and setters
    public String getSportName() {
        return sportName;
    }
    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
    public int getReservedCount() {
        return reservedCount;
    }
    public void setReservedCount(int reservedCount) {
        this.reservedCount = reservedCount;
    }
    public int getPlayedCount() {
        return playedCount;
    }
    public void setPlayedCount(int playedCount) {
        this.playedCount = playedCount;
    }

}
