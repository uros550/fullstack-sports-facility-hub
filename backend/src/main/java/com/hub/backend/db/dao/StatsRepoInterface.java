package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.EquipmentSpending;
import com.hub.backend.models.MonthlyActivity;
import com.hub.backend.models.SportReservationStats;

public interface StatsRepoInterface {
    
    List<SportReservationStats> getPlayedReservedPerSport();
    List<MonthlyActivity> getMonthlyActivity();
    List<EquipmentSpending> getEquipmentSpending();
    
}
