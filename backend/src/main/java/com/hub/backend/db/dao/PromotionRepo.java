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
        String query = "select p.*, sf.name as facilityName, s.name as sportName from promotion p join sportsFacility sf on p.facilityId = sf.id join sport s on s.id = p.sportId where now() between p.startDate and p.endDate limit 3";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
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
                    rs.getString("sportName"),
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

    @Override
    public List<Promotion> getPromotionsByEmployeeId(int employeeId) {

        List<Promotion> promotions = new ArrayList<>();
        String query = "select p.*, sf.name as facilityName, s.name as sportName from promotion p join sportsFacility sf on p.facilityId = sf.id join sport s on s.id = p.sportId join facilityemployee fe on fe.facilityId = sf.id where fe.employeeId = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, employeeId);

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
                    rs.getString("sportName"),
                    rs.getString("discountType"),
                    rs.getFloat("discountValue"),
                    startDate,
                    endDate
                );
                promotions.add(p);
            }    
        } catch (Exception e) {
            e.printStackTrace();
        }

        return promotions;
    }

    @Override
    public boolean createPromotion(Promotion newPromotion) {

        String query = "insert into promotion (name, facilityId, sportId, discountType, discountValue, startDate, endDate) values (?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ) {
            Timestamp startTs = (newPromotion.getStartDate() != null) ? Timestamp.valueOf(newPromotion.getStartDate()) : null;
            Timestamp endTs = (newPromotion.getEndDate() != null) ? Timestamp.valueOf(newPromotion.getEndDate()) : null;

            stm.setString(1, newPromotion.getName());
            stm.setInt(2, newPromotion.getFacilityId());
            stm.setInt(3, newPromotion.getSportId());
            stm.setString(4, newPromotion.getDiscountType());
            stm.setFloat(5, newPromotion.getDiscountValue());
            stm.setTimestamp(6, startTs);
            stm.setTimestamp(7, endTs);

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updatePromotion(Promotion newPromotion) {

        String query = "update promotion set name = ?, facilityId = ?, sportId = ?, discountType = ?, discountValue = ?, startDate = ?, endDate = ? where id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ) {
            Timestamp startTs = (newPromotion.getStartDate() != null) ? Timestamp.valueOf(newPromotion.getStartDate()) : null;
            Timestamp endTs = (newPromotion.getEndDate() != null) ? Timestamp.valueOf(newPromotion.getEndDate()) : null;

            stm.setString(1, newPromotion.getName());
            stm.setInt(2, newPromotion.getFacilityId());
            stm.setInt(3, newPromotion.getSportId());
            stm.setString(4, newPromotion.getDiscountType());
            stm.setFloat(5, newPromotion.getDiscountValue());
            stm.setTimestamp(6, startTs);
            stm.setTimestamp(7, endTs);
            stm.setInt(8, newPromotion.getId());

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Promotion getCurrentPromotion(int facilityId, int sportId) {
        String query = "select p.*, sf.name as facilityName, s.name as sportName " +
                       "from promotion p " +
                       "join sportsFacility sf on p.facilityId = sf.id " +
                       "join sport s on s.id = p.sportId " +
                       "where p.facilityId = ? and p.sportId = ? and now() between p.startDate and p.endDate " +
                       "limit 1";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ) {
            stm.setInt(1, facilityId);
            stm.setInt(2, sportId);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Timestamp startTs = rs.getTimestamp("startDate");
                LocalDateTime startDate = (startTs != null) ? startTs.toLocalDateTime() : null;

                Timestamp endTs = rs.getTimestamp("endDate");
                LocalDateTime endDate = (endTs != null) ? endTs.toLocalDateTime() : null;

                return new Promotion(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("facilityId"),
                    rs.getString("facilityName"),
                    rs.getInt("sportId"),
                    rs.getString("sportName"),
                    rs.getString("discountType"),
                    rs.getFloat("discountValue"),
                    startDate,
                    endDate
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
}
