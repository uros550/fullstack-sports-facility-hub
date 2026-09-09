package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.hub.backend.db.dao.FacilityReviewRepo;
import com.hub.backend.models.FacilityReview;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "http://localhost:4200")
public class FacilityReviewController {

    @GetMapping("/count/reservations/{athleteId}/{facilityId}")
    public int countConfirmedReservations(@PathVariable int athleteId, @PathVariable int facilityId) {
        return new FacilityReviewRepo().countConfirmedReservations(athleteId, facilityId);
    }
    
    @GetMapping("/count/{athleteId}/{facilityId}")
    public int countAthleteReviews(@PathVariable int athleteId, @PathVariable int facilityId) {
        return new FacilityReviewRepo().countAthleteReviews(athleteId, facilityId);
    }

    @GetMapping("/top5/{facilityId}")
    public List<FacilityReview> getTopReviewsByFacId(@PathVariable int facilityId) {
        return new FacilityReviewRepo().getTopReviewsByFacId(facilityId);
    }

    @PostMapping("/save")
    public boolean saveReview(@RequestBody FacilityReview review) {
        return new FacilityReviewRepo().saveReview(review);
    }
    
}
