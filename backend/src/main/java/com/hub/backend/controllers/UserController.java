package com.hub.backend.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hub.backend.db.dao.UserRepo;
import com.hub.backend.models.User;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200/")
public class UserController {
    
    @PostMapping("/login")
    public User login(@RequestBody User u) {
        return new UserRepo().login(u);
    }

    @PostMapping("/register")
    public String register(@RequestBody User u) {
        return new UserRepo().register(u);
    }

    @PostMapping("/upload-image")
    public String uploadProfilePicture(@RequestParam("username") String username, @RequestParam("image") MultipartFile file) {
        return new UserRepo().saveProfilePicture(username, file);
    }

}
