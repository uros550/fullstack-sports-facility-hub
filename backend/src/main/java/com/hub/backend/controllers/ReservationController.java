package com.hub.backend.controllers;

import java.time.LocalDate;
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

import com.hub.backend.db.dao.ReservationRepo;
import com.hub.backend.models.Application;
import com.hub.backend.models.AvailabilitySlot;
import com.hub.backend.models.Reservation;

@RestController
@RequestMapping("users/reservations")
@CrossOrigin(origins = "http://localhost:4200/")
public class ReservationController {

    @GetMapping("/employee/{employeeId}")
    public List<Reservation> getActiveReservationsByEmployeeId(@PathVariable int employeeId) {
        return new ReservationRepo().getActiveReservationsByEmployeeId(employeeId);
    }

    @PostMapping("/accept/{id}")
    public String acceptReservation(@PathVariable int id) {
        boolean success = new ReservationRepo().acceptReservation(id);
        return success ? "Success" : "Error";
    }

    @PostMapping("/reject/{id}")
    public String rejectReservation(@PathVariable int id) {
        boolean success = new ReservationRepo().rejectReservation(id);
        return success ? "Success" : "Error";
    }
    
    @PostMapping("/cancel/{id}")
    public String cancelReservation(@PathVariable int id) {
        boolean success = new ReservationRepo().cancelReservation(id);
        return success ? "Success" : "Error";
    }

    @PostMapping("/noShow/{id}")
    public String noShowReservation(@PathVariable int id, @RequestParam int athleteId, @RequestParam int facilityId) {  
        boolean success = new ReservationRepo().noShowReservation(id, athleteId, facilityId);
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

    @GetMapping("/exploreAds/{athleteId}")
    public List<Reservation> getReservationAds(@PathVariable int athleteId) {
        return new ReservationRepo().getReservationAds(athleteId);
    }

    @GetMapping("/checkStatus/{athleteId}")
    public List<Application> getAllApplicationsForAthlete(@PathVariable int athleteId) {
        return new ReservationRepo().getAllApplicationsForAthlete(athleteId);
    }

    @PutMapping("/apply/{id}/{athleteId}")
    public boolean applyToAd(@PathVariable int id, @PathVariable int athleteId) {
        return new ReservationRepo().applyToAd(id, athleteId);
    }

    @PostMapping("/changeStatus")
    public boolean acceptRejectApp(@RequestParam int reservationId, @RequestParam int athleteId, @RequestParam boolean accept) {
        return new ReservationRepo().acceptRejectApp(reservationId, athleteId, accept);
    }

    @GetMapping("/applications/{athleteId}")
    public List<Application> getAllApplicationsByAthlete(@PathVariable int athleteId) {
        return new ReservationRepo().getAllApplicationsByAthlete(athleteId);
    }

}
