package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hub.backend.db.dao.AdminRepo;
import com.hub.backend.models.SportsFacility;
import com.hub.backend.models.User;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {
    
    @GetMapping("/users/all")
    public List<User> getAllUsers() {
        return new AdminRepo().getAllUsers();
    }

    @GetMapping("/users/pending")
    public List<User> getPendingUsers() {
        return new AdminRepo().getPendingUsers();
    }

    @PostMapping("/users/update/{userId}")
    public boolean changeUsername(@PathVariable int userId, @RequestParam String newUsername) {
        if (newUsername == null || newUsername.trim().isEmpty()) return false;
        return new AdminRepo().changeUsername(userId, newUsername);
    }

    @PostMapping("/users/accept/{userId}")
    public boolean acceptRegistration(@PathVariable int userId) {
        return new AdminRepo().acceptRegistration(userId);
    }

    @PostMapping("/users/reject/{userId}")
    public boolean rejectRegistration(@PathVariable int userId) {
        return new AdminRepo().rejectRegistration(userId);
    }

    @PostMapping("/users/delete/{userId}")
    public boolean deleteAccount(@PathVariable int userId) {
        return new AdminRepo().deleteAccount(userId);
    }

    @GetMapping("/facilities/pending")
    public List<SportsFacility> getPendingFacilities() {
        return new AdminRepo().getPendingFacilities();
    }

    @PostMapping("/facilities/accept/{facilityId}")
    public boolean acceptFacility(@PathVariable int facilityId) {
        return new AdminRepo().acceptFacility(facilityId);
    }

    @PostMapping("/facilities/reject/{facilityId}")
    public boolean rejectFacility(@PathVariable int facilityId) {
        return new AdminRepo().rejectFacility(facilityId);
    }

}
