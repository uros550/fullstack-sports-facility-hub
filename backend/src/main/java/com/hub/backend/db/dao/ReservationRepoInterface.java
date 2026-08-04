package com.hub.backend.db.dao;

import java.time.LocalDate;
import java.util.List;

import com.hub.backend.models.Application;
import com.hub.backend.models.AvailabilitySlot;
import com.hub.backend.models.Reservation;

public interface ReservationRepoInterface {
    
    List<Reservation> getReservationsByAthleteId(int athleteId);
    List<Reservation> getActiveReservationsByAthleteId(int athleteId);
    boolean cancelReservation(int reservationId);
    String addReservation(Reservation newReservation);
    List<AvailabilitySlot> getAvailabilityByCourtAndDate(int courtId, LocalDate date);
    int updateMissingPlayers(int missingPlayer, int reservationId);
    List<Reservation> getReservationAds(int athleteId);
    List<Application> getAllApplicationsForAthlete(int athleteId);
    boolean applyToAd(int reservationId, int athleteId);
    boolean acceptRejectApp(int reservationId, int athleteId, boolean accept);
    List<Application> getAllApplicationsByAthlete(int athleteId);

}
