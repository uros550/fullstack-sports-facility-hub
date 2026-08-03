package com.hub.backend.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hub.backend.db.dao.ReservationRepo;
import com.hub.backend.models.AvailabilitySlot;
import com.hub.backend.models.Reservation;

@RestController
@RequestMapping("users/reservations")
@CrossOrigin(origins = "http://localhost:4200/")
public class ReservationController {
    
    @PostMapping("/cancel/{id}")
    public String cancelReservation(@PathVariable int id) {
        boolean success = new ReservationRepo().cancelReservation(id);
        return success ? "Success" : "Error";
    }

    @GetMapping("/availability/{courtId}")
    public List<AvailabilitySlot> getAvailability(@PathVariable int courtId, @RequestParam String date) {
        LocalDate selectedDate = LocalDate.parse(date);
        return new ReservationRepo().getAvailabilityByCourtAndDate(courtId, selectedDate);
    }

    @PostMapping("/create")
    public String addReservation(@RequestBody Reservation newReservation) {
        return new ReservationRepo().addReservation(newReservation);
    }

    @PostMapping("/updateMP/{id}/{missingPlayers}")
    public int updateMissingPlayers(@PathVariable int missingPlayers, @PathVariable int id) {
        return new ReservationRepo().updateMissingPlayers(missingPlayers, id);
    }

}
