package com.hub.backend.models;

public class OrderItem {

    private int id;
    private int orderId;
    private int equipmentId;
    private String equipmentName;
    private String equipmentImage;
    private int quantity;
    private float priceAtPurchase;

    //constructors
    public OrderItem() {}

    public OrderItem(int id, int orderId, int equipmentId, String equipmentName, String equipmentImage, int quantity,
            float priceAtPurchase) {
        this.id = id;
        this.orderId = orderId;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.equipmentImage = equipmentImage;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public int getEquipmentId() {
        return equipmentId;
    }
    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }
    public String getEquipmentName() {
        return equipmentName;
    }
    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
    public String getEquipmentImage() {
        return equipmentImage;
    }
    public void setEquipmentImage(String equipmentImage) {
        this.equipmentImage = equipmentImage;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public float getPriceAtPurchase() {
        return priceAtPurchase;
    }
    public void setPriceAtPurchase(float priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    } 
    
}
