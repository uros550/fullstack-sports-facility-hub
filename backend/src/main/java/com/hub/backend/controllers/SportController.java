package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hub.backend.db.dao.SportRepo;
import com.hub.backend.models.Sport;

@RestController
@RequestMapping("/sports")
@CrossOrigin(origins = "http://localhost:4200")
public class SportController {

    @GetMapping
    public List<Sport> getAllSports() {
        return new SportRepo().getAllSports();
    }

    @GetMapping("/requiredPlayers/{sportId}")
    public int getMaxMissingPlayers(@PathVariable int sportId) {
        return new SportRepo().getMaxMissingPlayers(sportId);
    }

    @GetMapping("/facility/{facilityId}")
    public List<Sport> getSportsByFacilityId(@PathVariable int facilityId) {
        return new SportRepo().getSportsByFacilityId(facilityId);
    }

    @PostMapping("/add")
    public boolean addSport(@RequestBody Sport newSport) {
        return new SportRepo().addSport(newSport);
    }

}
