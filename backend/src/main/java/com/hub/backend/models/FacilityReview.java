package com.hub.backend.models;

import java.time.LocalDateTime;

public class FacilityReview {

    private int id;
    private int facilityId;
    private String facilityName;
    private int athleteId;
    private String athleteName;
    private boolean liked;
    private String comment;
    private LocalDateTime reviewDate;

    //constructors
    public FacilityReview() {}
    
    public FacilityReview(int id, int facilityId, String facilityName, int athleteId, String athleteName, boolean liked, String comment, LocalDateTime reviewDate) {
        this.id = id;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.athleteId = athleteId;
        this.athleteName = athleteName;
        this.liked = liked;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getFacilityId() {
        return facilityId;
    }
    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }
    public String getFacilityName() {
        return facilityName;
    }
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }
    public int getAthleteId() {
        return athleteId;
    }
    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }
    public String getAthleteName() {
        return athleteName;
    }
    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }
    public boolean isLiked() {
        return liked;
    }
    public void setLiked(boolean liked) {
        this.liked = liked;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public LocalDateTime getReviewDate() {
        return reviewDate;
    }
    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

}
