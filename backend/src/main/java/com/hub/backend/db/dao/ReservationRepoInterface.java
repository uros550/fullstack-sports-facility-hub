package com.hub.backend.db.dao;

import java.time.LocalDate;
import java.util.List;

import com.hub.backend.models.AvailabilitySlot;
import com.hub.backend.models.Reservation;

public interface ReservationRepoInterface {
    
    List<Reservation> getReservationsByAthleteId(int athleteId);
    boolean cancelReservation(int reservationId);
    String addReservation(Reservation newReservation);
    List<AvailabilitySlot> getAvailabilityByCourtAndDate(int courtId, LocalDate date);

}
