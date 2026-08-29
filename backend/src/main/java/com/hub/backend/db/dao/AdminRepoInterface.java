package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.User;

public interface AdminRepoInterface {
    
    List<User> getAllUsers();
    List<User> getPendingUsers();
    boolean changeUsername(int userId, String newUsername);
    boolean acceptRegistration(int userId);
    boolean rejectRegistration(int userId);
    boolean deleteAccount(int userId);

}