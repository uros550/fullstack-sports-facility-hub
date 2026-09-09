package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hub.backend.db.dao.ReservationRepo;
import com.hub.backend.db.dao.UserRepo;
import com.hub.backend.models.AthleteProfile;
import com.hub.backend.models.Reservation;
import com.hub.backend.models.User;
import com.hub.backend.models.ForgotPasswordRequest;
import com.hub.backend.models.ForgotPasswordResponse;
import com.hub.backend.models.ResetPasswordRequest;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    
    @PostMapping("/login")
    public User login(@RequestBody User u) {
        return new UserRepo().login(u);
    }

    @PostMapping("/register")
    public String register(@RequestBody User u) {
        return new UserRepo().register(u);
    }

    @PutMapping("/upload-image")
    public String uploadProfilePicture(@RequestParam("username") String username, @RequestParam("image") MultipartFile file) {
        return new UserRepo().saveProfilePicture(username, file);
    }

    @GetMapping("/profile/{id}")
    public AthleteProfile getProfile(@PathVariable int id) {
        return new UserRepo().getProfileById(id);
    }

    @GetMapping("/sports/{id}")
    public List<Integer> getUserSportIds(@PathVariable int id) {
        return new UserRepo().getUserSportIds(id);
    }

    //update user profile
    @PutMapping("/profile")
    public String updateProfile(@RequestBody AthleteProfile profile) {
        return new UserRepo().updateProfile(profile);
    }

    @PutMapping("/remove-image")
    public String removeProfilePicture(@RequestBody String username) {
        return new UserRepo().removeProfilePicture(username);
    }
    
    //update user sports
    @PutMapping("/sports/{id}")
    public String updateUserSportIds(@PathVariable int id, @RequestBody List<Integer> sportIds) {
        return new UserRepo().updateUserSportIds(id, sportIds);
    }

    @GetMapping("/reservations/{id}")
    public List<Reservation> getReservations(@PathVariable int id) {
        return new ReservationRepo().getReservationsByAthleteId(id);
    }

    @GetMapping("/reservations/active/{id}")
    public List<Reservation> getActiveReservations(@PathVariable int id) {
        return new ReservationRepo().getActiveReservationsByAthleteId(id);
    }

    @GetMapping("/reservations/blocked/{userId}/{facilityId}")
    public boolean checkIfBlocked(@PathVariable int userId, @PathVariable int facilityId) {
        return new UserRepo().checkIfBlocked(userId, facilityId);
    }

    @PostMapping("/forgot-password")
    public ForgotPasswordResponse forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return new UserRepo().forgotPassword(request.getUsernameOrEmail());
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        return new UserRepo().resetPassword(request.getToken(), request.getNewPassword());
    }

    @PostMapping("/request-password-change/{userId}")
    public ForgotPasswordResponse requestPasswordChange(@PathVariable int userId) {
        return new UserRepo().requestPasswordChangeForUser(userId);
    }
}
