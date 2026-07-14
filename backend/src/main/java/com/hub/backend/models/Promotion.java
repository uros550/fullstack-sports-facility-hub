package com.hub.backend.models;

import java.time.LocalDateTime;

public class Promotion {
    private int id;
    private String name;
    private int facilityId;
    private String facilityName;
    private int sportId;
    private String discountType;
    private float discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    //constructors
    public Promotion() {
    }

    public Promotion(int id, String name, int facilityId, String facilityName, int sportId, String discountType, float discountValue,
            LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.name = name;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.sportId = sportId;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public int getSportId() {
        return sportId;
    }
    public void setSportId(int sportId) {
        this.sportId = sportId;
    }
    public String getDiscountType() {
        return discountType;
    }
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
    public float getDiscountValue() {
        return discountValue;
    }
    public void setDiscountValue(float discountValue) {
        this.discountValue = discountValue;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
