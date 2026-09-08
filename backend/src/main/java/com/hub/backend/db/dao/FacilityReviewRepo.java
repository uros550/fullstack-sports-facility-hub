package com.hub.backend.db.dao;


import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
import com.hub.backend.models.FacilityReview;

public class FacilityReviewRepo implements FacilityReviewRepoInterface {

    @Override
    public int countConfirmedReservations(int athleteId, int facilityId) {
        
        String query = "select count(*) from reservation where athleteId = ? and facilityId = ? and status = 'CONFIRMED' and endTime < NOW()";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, athleteId);
            stm.setInt(2, facilityId);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public int countAthleteReviews(int athleteId, int facilityId) {

        String query = "select count(*) from facilityreview where athleteId = ? and facilityId = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, athleteId);
            stm.setInt(2, facilityId);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public List<FacilityReview> getTopReviewsByFacId(int facilityId) {
        
        List<FacilityReview> reviews = new ArrayList<>();
        String query = "SELECT fr.*, sf.name AS facilityName, u.username AS athleteName " +
                    "FROM FacilityReview fr " +
                    "JOIN SportsFacility sf ON sf.id = fr.facilityId " +
                    "JOIN User u ON u.id = fr.athleteId " +
                    "WHERE fr.facilityId = ? " +
                    "ORDER BY fr.reviewDate DESC LIMIT 5";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ) {
            stm.setInt(1, facilityId);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {

                Timestamp reviewTs = rs.getTimestamp("reviewDate");
                LocalDateTime reviewDate = (reviewTs != null) ? reviewTs.toLocalDateTime() : null;

                FacilityReview fr = new FacilityReview(
                    rs.getInt("id"),
                    rs.getInt("facilityId"),
                    rs.getString("facilityName"),
                    rs.getInt("athleteId"),
                    rs.getString("athleteName"),
                    rs.getBoolean("liked"),
                    rs.getString("comment"),
                    reviewDate
                );
                reviews.add(fr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviews;
    }

    @Override
    public boolean saveReview(FacilityReview review) {

        int reservations = countConfirmedReservations(review.getAthleteId(), review.getFacilityId());
        int reviews = countAthleteReviews(review.getAthleteId(), review.getFacilityId());
        
        if (reviews >= reservations) {
            return false;
        }

        String insertQuery = "INSERT INTO FacilityReview (facilityId, athleteId, liked, comment, reviewDate) VALUES (?, ?, ?, ?, NOW())";
        String updateLikesQuery = "UPDATE SportsFacility SET likesCount = likesCount + ? WHERE id = ?";
        
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement insertStm = conn.prepareStatement(insertQuery);
            PreparedStatement updateStm = conn.prepareStatement(updateLikesQuery);
        ){
            conn.setAutoCommit(false);

            //insert part
            insertStm.setInt(1, review.getFacilityId());
            insertStm.setInt(2, review.getAthleteId());
            insertStm.setBoolean(3, review.isLiked());
            insertStm.setString(4, review.getComment());

            int rowsInserted = insertStm.executeUpdate();
            if (rowsInserted == 0) {
                conn.rollback();
                return false;
            }
            
            //update part
            int change = review.isLiked() ? 1 : -1;
            updateStm.setInt(1, change);
            updateStm.setInt(2, review.getFacilityId());
            int rowsUpdated = updateStm.executeUpdate();
            if (rowsUpdated == 0) {
                conn.rollback();
                return false;
            }
            
            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
