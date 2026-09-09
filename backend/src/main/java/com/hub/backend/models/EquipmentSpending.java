package com.hub.backend.models;

public class EquipmentSpending {
    
    private String month;
    private double totalSpent;
    
    public EquipmentSpending() {}

    public EquipmentSpending(String month, double totalSpent) {
        this.month = month;
        this.totalSpent = totalSpent;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }
}