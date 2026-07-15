package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.Court;
import com.hub.backend.models.SearchFacilitiesRequest;
import com.hub.backend.models.SportsFacility;

public interface SportsFacilityRepoInterface {

    List<SportsFacility> getAllFacilites();
    List<SportsFacility> getAllActiveFacilities();
    int getActiveFacilitiesCount();
    List<SportsFacility> getTop3Facilities();
    List<String> getAllCities();
    List<SportsFacility> searchFacilities(SearchFacilitiesRequest request);
    public SportsFacility getFacilityById(int id);
    List<Court> getCourtsByFacilityId(int facilityId);

}
