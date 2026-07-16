package com.hub.backend.models;

import java.util.List;

public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String profilePicture;
    private String role;
    private String status;

    //registration
    private String facilityName;
    private String facilityAddress;
    private String facilityCity;
    private String facilityMb;
    private String facilityPib;
    private List<Integer> sports;

    //constructors
    public User() {}

    public User(int id, String firstName, String lastName, String username, String email, String password, String phone,String profilePicture, String role, String status) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.profilePicture = profilePicture;
        this.role = role;
        this.status = status;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    //registration getters and setters
    public String getFacilityName() {
        return facilityName;
    }
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }
    public String getFacilityAddress() {
        return facilityAddress;
    }
    public void setFacilityAddress(String facilityAddress) {
        this.facilityAddress = facilityAddress;
    }
    public String getFacilityCity() {
        return facilityCity;
    }
    public void setFacilityCity(String facilityCity) {
        this.facilityCity = facilityCity;
    }
    public String getFacilityMb() {
        return facilityMb;
    }
    public void setFacilityMb(String facilityMb) {
        this.facilityMb = facilityMb;
    }
    public String getFacilityPib() {
        return facilityPib;
    }
    public void setFacilityPib(String facilityPib) {
        this.facilityPib = facilityPib;
    }
    public List<Integer> getSports() {
        return sports;
    }
    public void setSports(List<Integer> sports) {
        this.sports = sports;
    }

}