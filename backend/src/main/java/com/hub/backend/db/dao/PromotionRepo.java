package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
import com.hub.backend.models.Promotion;

public class PromotionRepo implements PromotionRepoInterface {

    @Override
    public List<Promotion> getActivePromotions() {
        
        List<Promotion> activePromotions = new ArrayList<>();
        String query = "select p.*, sf.name as facilityName from promotion p join sportsFacility sf on p.facilityId = sf.id where now() between p.startDate and p.endDate limit 3";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                
                Timestamp startTs = rs.getTimestamp("startDate");
                LocalDateTime startDate = (startTs != null) ? startTs.toLocalDateTime() : null;
                
                Timestamp endTs = rs.getTimestamp("endDate");
                LocalDateTime endDate = (endTs != null) ? endTs.toLocalDateTime() : null;

                Promotion p = new Promotion(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("facilityId"),
                    rs.getString("facilityName"),
                    rs.getInt("sportId"),
                    rs.getString("discountType"),
                    rs.getFloat("discountValue"),
                    startDate,
                    endDate
                );
                activePromotions.add(p);
            }    

        } catch (Exception e) {
            e.printStackTrace();
        }

        return activePromotions;
    }
    
}
