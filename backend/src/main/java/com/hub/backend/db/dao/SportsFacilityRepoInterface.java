package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.Court;
import com.hub.backend.models.SearchFacilitiesRequest;
import com.hub.backend.models.SportsFacility;

public interface SportsFacilityRepoInterface {

    List<SportsFacility> getAllFacilites();
    List<SportsFacility> getAllActiveFacilities();
    List<SportsFacility> getTop3Facilities();
    SportsFacility getFacilityById(int id);
    int getActiveFacilitiesCount();
    List<Court> getCourtsByFacilityId(int facilityId);
    List<Court> getCourtsByFacilitySport(int facilityId, int sportId);
    List<String> getImagesByFacilityId(int facilityId);
    List<String> getAllCities();
    List<SportsFacility> searchFacilities(SearchFacilitiesRequest request);

}
