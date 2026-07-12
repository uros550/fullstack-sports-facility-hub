package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hub.backend.db.dao.SportsFacilityRepo;
import com.hub.backend.models.SportsFacility;

@RestController
@RequestMapping("/sportsFacilities")
@CrossOrigin(origins = "http://localhost:4200")
public class SportsFacilityController {
    
    @GetMapping
    public List<SportsFacility> getAllFacilities() {
        return new SportsFacilityRepo().getAllFacilites();
    }

}
