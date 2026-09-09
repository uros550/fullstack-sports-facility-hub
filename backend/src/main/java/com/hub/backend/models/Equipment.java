package com.hub.backend.models;

public class Equipment {
    
    private int id;
    private String name;
    private int sportId;
    private String sportName;
    private float price;
    private int stock;
    private String imageUrl;
    
    //constructors
    public Equipment() {}

    public Equipment(int id, String name, int sportId, String sportName, float price, int stock, String imageUrl) {
        this.id = id;
        this.name = name;
        this.sportId = sportId;
        this.sportName = sportName;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
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
    public int getSportId() {
        return sportId;
    }
    public void setSportId(int sportId) {
        this.sportId = sportId;
    }
    public String getSportName() {
        return sportName;
    }
    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
}
