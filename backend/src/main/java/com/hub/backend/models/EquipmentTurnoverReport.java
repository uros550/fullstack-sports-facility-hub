package com.hub.backend.models;

public class EquipmentTurnoverReport {
    private String equipmentName;
    private int totalQuantitySold;
    private double totalRevenue;

    //consturctors
    public EquipmentTurnoverReport() {}

    public EquipmentTurnoverReport(String equipmentName, int totalQuantitySold, double totalRevenue) {
        this.equipmentName = equipmentName;
        this.totalQuantitySold = totalQuantitySold;
        this.totalRevenue = totalRevenue;
    }

    //getters and setters
    public String getEquipmentName() {
        return equipmentName;
    }
    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
    public int getTotalQuantitySold() {
        return totalQuantitySold;
    }
    public void setTotalQuantitySold(int totalQuantitySold) {
        this.totalQuantitySold = totalQuantitySold;
    }
    public double getTotalRevenue() {
        return totalRevenue;
    }
    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

}