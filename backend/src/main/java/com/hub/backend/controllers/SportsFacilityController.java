package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hub.backend.db.dao.SportsFacilityRepo;
import com.hub.backend.models.SearchFacilitiesRequest;
import com.hub.backend.models.SportsFacility;

@RestController
@RequestMapping("/sportsFacilities")
@CrossOrigin(origins = "http://localhost:4200")
public class SportsFacilityController {
    
    @GetMapping
    public List<SportsFacility> getAllFacilities() {
        return new SportsFacilityRepo().getAllFacilites();
    }

    @GetMapping("/active")
    public List<SportsFacility> getAllActiveFacilities() {
        return new SportsFacilityRepo().getAllActiveFacilities();
    }

    @GetMapping("/count")
    public int getActiveFacilitiesCount() {
        return new SportsFacilityRepo().getActiveFacilitiesCount();
    }

    @GetMapping("/top3")
    public List<SportsFacility> getTop3Facilities() {
        return new SportsFacilityRepo().getTop3Facilities();
    }

    @GetMapping("/cities")
    public List<String> getAllCities() {
        return new SportsFacilityRepo().getAllCities();
    }

    @PostMapping("/search")
    public List<SportsFacility> searchFacilities(@RequestBody SearchFacilitiesRequest request) {
        return new SportsFacilityRepo().searchFacilities(request);
    }

    @GetMapping("/{id}")
    public SportsFacility getFacilityById(@PathVariable int id) {
        return new SportsFacilityRepo().getFacilityById(id);
    }
}
