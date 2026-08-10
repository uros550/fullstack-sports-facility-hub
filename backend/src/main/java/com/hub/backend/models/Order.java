package com.hub.backend.models;

import java.time.LocalDateTime;

public class Order {
    
    private int id;
    private int athleteId;
    private LocalDateTime orderDate;
    private String status;
    
    //constructors
    public Order() {}

    public Order(int id, int athleteId, LocalDateTime orderDate, String status) {
        this.id = id;
        this.athleteId = athleteId;
        this.orderDate = orderDate;
        this.status = status;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAthleteId() {
        return athleteId;
    }
    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}
