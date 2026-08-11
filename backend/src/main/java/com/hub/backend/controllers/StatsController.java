package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hub.backend.db.dao.StatsRepo;
import com.hub.backend.models.SportReservationStats;

@RestController
@RequestMapping("/stats")
@CrossOrigin(origins = "http://localhost:4200")
public class StatsController {
    
    @GetMapping("/played/reserved/perSport")
    public List<SportReservationStats> getPlayedReservedPerSport() {
        return new StatsRepo().getPlayedReservedPerSport();
    }

}
