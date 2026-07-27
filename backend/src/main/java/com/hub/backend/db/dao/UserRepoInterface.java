package com.hub.backend.db.dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hub.backend.models.AthleteProfile;
import com.hub.backend.models.User;

public interface UserRepoInterface {

    User login(User user);
    String register(User user);
    String saveProfilePicture(String username, MultipartFile file);
    String updateProfilePicture(String username, String imagePath);
    AthleteProfile getProfileById(int id);
    AthleteProfile getProfileByUsername(String username);
    List<Integer> getUserSportIds(int userId);
    String updateProfile(AthleteProfile newProfile);
    String updateUserSportIds(int userId, List<Integer> newSportIds);
    String removeProfilePicture(String username);

}
