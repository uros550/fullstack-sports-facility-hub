package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.FacilityReview;

public interface FacilityReviewRepoInterface {
    
    int countConfirmedReservations(int athleteId, int facilityId);
    int countAthleteReviews(int athleteId, int facilityId);
    List<FacilityReview> getTopReviewsByFacId(int facilityId);
    boolean saveReview(FacilityReview review);

}
