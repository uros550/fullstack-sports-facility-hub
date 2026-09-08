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
import com.hub.backend.models.Court;
import com.hub.backend.models.SearchFacilitiesRequest;
import com.hub.backend.models.SportsFacility;
import com.hub.backend.models.AddFacilityRequest;

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

    @GetMapping("/top3") //active top3
    public List<SportsFacility> getTop3Facilities() {
        return new SportsFacilityRepo().getTop3Facilities();
    }

    @GetMapping("/{id}")
    public SportsFacility getFacilityById(@PathVariable int id) {
        return new SportsFacilityRepo().getFacilityById(id);
    }

    @GetMapping("/employee/{employeeId}")
    public List<SportsFacility> getFacilitiesFromEmployee(@PathVariable int employeeId) {
        return new SportsFacilityRepo().getFacilitiesForEmployee(employeeId);
    }

    @GetMapping("/count")
    public int getActiveFacilitiesCount() {
        return new SportsFacilityRepo().getActiveFacilitiesCount();
    }
    
    @GetMapping("/courts/{facilityId}")
    public List<Court> getCourtsByFacilityId(@PathVariable int facilityId) {
        return new SportsFacilityRepo().getCourtsByFacilityId(facilityId);
    }

    @GetMapping("/courts/{facilityId}/{sportId}")
    public List<Court> getCourtsByFacilitySport(@PathVariable int facilityId, @PathVariable int sportId) {
        return new SportsFacilityRepo().getCourtsByFacilitySport(facilityId, sportId);
    }

    @GetMapping("/images/{facilityId}")
    public List<String> getImagesByFacilityId(@PathVariable int facilityId) {
        return new SportsFacilityRepo().getImagesByFacilityId(facilityId);
    }

    @GetMapping("/cities")
    public List<String> getAllCities() {
        return new SportsFacilityRepo().getAllCities();
    }

    @PostMapping("/search")
    public List<SportsFacility> searchFacilities(@RequestBody SearchFacilitiesRequest request) {
        return new SportsFacilityRepo().searchFacilities(request);
    }

    @PostMapping("/add/{employeeId}")
    public String addFacility(@RequestBody AddFacilityRequest request, @PathVariable int employeeId) {
        return new SportsFacilityRepo().addFacility(request.getFacility(), request.getCourts(), employeeId);
    }

    @PostMapping("/update")
    public boolean updateFacility(@RequestBody SportsFacility facility) {
        return new SportsFacilityRepo().updateFacility(facility);
    }

    @PostMapping("/add/court")
    public boolean addCourt(@RequestBody Court court) {
        return new SportsFacilityRepo().addCourt(court);
    }

    @PostMapping("/update/court")
    public boolean updateCourt(@RequestBody Court court) {
        return new SportsFacilityRepo().updateCourt(court);
    }

}
