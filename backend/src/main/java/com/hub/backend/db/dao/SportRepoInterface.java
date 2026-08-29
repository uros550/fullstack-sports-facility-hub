package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.Sport;

public interface SportRepoInterface {

    List<Sport> getAllSports();
    int getMaxMissingPlayers(int sportId);
    List<Sport> getSportsByFacilityId(int facilityId);
    boolean addSport(Sport newSport);

}
