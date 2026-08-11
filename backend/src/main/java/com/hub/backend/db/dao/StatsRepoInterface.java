package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.SportReservationStats;

public interface StatsRepoInterface {
    
    List<SportReservationStats> getPlayedReservedPerSport();

}
