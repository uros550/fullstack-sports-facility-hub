package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.Reservation;

public interface ReservationRepoInterface {
    
    List<Reservation> getReservationsByAthleteId(int athleteId);
    boolean cancelReservation(int reservationId);

}
