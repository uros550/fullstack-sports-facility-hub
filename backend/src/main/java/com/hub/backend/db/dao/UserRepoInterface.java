package com.hub.backend.db.dao;

import org.springframework.web.multipart.MultipartFile;

import com.hub.backend.models.User;

public interface UserRepoInterface {

    User login(User user);
    String register(User user);
    String saveProfilePicture(String username, MultipartFile file);
    String updateProfilePicture(String username, String imagePath);

}
