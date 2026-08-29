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
import com.hub.backend.models.User;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {
    
    @GetMapping("/users/all")
    public List<User> getAllUsers() {
        return new AdminRepo().getAllUsers();
    }

    @PostMapping("/users/update/{userId}")
    public boolean changeUsername(@PathVariable int userId, @RequestParam String newUsername) {
        if (newUsername == null || newUsername.trim().isEmpty()) return false;
        return new AdminRepo().changeUsername(userId, newUsername);
    }

    @PostMapping("/users/delete/{userId}")
    public boolean deleteAccount(@PathVariable int userId) {
        return new AdminRepo().deleteAccount(userId);
    }

}
