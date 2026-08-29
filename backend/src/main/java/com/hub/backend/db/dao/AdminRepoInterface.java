package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.User;

public interface AdminRepoInterface {
    
    List<User> getAllUsers();
    boolean changeUsername(int userId, String newUsername);
    boolean deleteAccount(int userId);

}