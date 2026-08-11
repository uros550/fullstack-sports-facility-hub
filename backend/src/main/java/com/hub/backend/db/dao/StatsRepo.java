package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
import com.hub.backend.models.EquipmentSpending;
import com.hub.backend.models.MonthlyActivity;
import com.hub.backend.models.SportReservationStats;

public class StatsRepo implements StatsRepoInterface {

    @Override
    public List<SportReservationStats> getPlayedReservedPerSport() {
        List<SportReservationStats> stats = new ArrayList<>();
        
        String query =  "SELECT s.name AS sportName, " +
                        "COUNT(r.id) AS reservedCount, " +
                        "SUM(CASE WHEN r.status = 'CONFIRMED' AND r.endTime < NOW() THEN 1 ELSE 0 END) AS playedCount " +
                        "FROM reservation r " +
                        "JOIN sport s ON r.sportId = s.id " +
                        "GROUP BY s.id, s.name";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                stats.add(new SportReservationStats(
                    rs.getString("sportName"),
                    rs.getInt("reservedCount"),
                    rs.getInt("playedCount")
                ));
            }

            return stats;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stats;
    }

    @Override
    public List<MonthlyActivity> getMonthlyActivity() {

        List<MonthlyActivity> stats = new ArrayList<>();
        String query = "SELECT DATE_FORMAT(r.startTime, '%Y-%m') AS month, " + //as 2026-08 for example
                       "COUNT(r.id) AS activityCount " +
                       "FROM reservation r " +
                       "WHERE YEAR(r.startTime) = YEAR(CURRENT_DATE) AND r.status = 'CONFIRMED' " +
                       "GROUP BY DATE_FORMAT(r.startTime, '%Y-%m') " +
                       "ORDER BY month ASC";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                MonthlyActivity ma = new MonthlyActivity(
                    rs.getString("month"),
                    rs.getInt("activityCount")
                );
                stats.add(ma);
            }

            return stats;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stats;
    }

    @Override
    public List<EquipmentSpending> getEquipmentSpending() {
        List<EquipmentSpending> stats = new ArrayList<>();

        String query = "SELECT DATE_FORMAT(o.orderDate, '%Y-%m') AS month, " +
                       "SUM(oi.quantity * oi.priceAtPurchase) AS totalSpent " +
                       "FROM `Order` o " +
                       "JOIN orderItem oi ON o.id = oi.orderId " +
                       "WHERE o.status = 'PICKED_UP' AND YEAR(o.orderDate) = YEAR(CURRENT_DATE) " +
                       "GROUP BY DATE_FORMAT(o.orderDate, '%Y-%m') " +
                       "ORDER BY month ASC";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                EquipmentSpending es = new EquipmentSpending(
                    rs.getString("month"),
                    rs.getDouble("totalSpent")
                );
                stats.add(es);
            }

            return stats;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stats;
    }
        
}
