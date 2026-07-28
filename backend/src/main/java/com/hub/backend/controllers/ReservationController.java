package com.hub.backend.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hub.backend.db.dao.ReservationRepo;

@RestController
@RequestMapping("users/reservations")
@CrossOrigin(origins = "http://localhost:4200/")
public class ReservationController {
    
    @PostMapping("/cancel/{id}")
    public String cancelReservation(@PathVariable int id) {
        boolean success = new ReservationRepo().cancelReservation(id);
        return success ? "Success" : "Error";
    }

}
