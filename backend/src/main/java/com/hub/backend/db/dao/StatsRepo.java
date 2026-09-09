package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
import com.hub.backend.models.AvailabilitySlot;
import com.hub.backend.models.Court;
import com.hub.backend.models.CourtOccupancyReport;
import com.hub.backend.models.EquipmentSpending;
import com.hub.backend.models.EquipmentTurnoverReport;
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
        
    @Override
    public List<CourtOccupancyReport> getCourtOccupancyReport(int facilityId, int year, int month) {
       
        List<CourtOccupancyReport> list = new ArrayList<>(); 
        //get courts for selected facility
        List<Court> courts = new SportsFacilityRepo().getCourtsByFacilityId(facilityId);
        
        //get selected month length
        java.time.YearMonth yearMonth = java.time.YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();

        for (Court court : courts) {
            int totalSlotsCount = 0;
            int takenSlotsCount = 0;

            for (int day = 1; day <= daysInMonth; day++) {
                LocalDate date = LocalDate.of(year, month, day);
                
                //get slots for a day
                List<AvailabilitySlot> slots = new ReservationRepo().getAvailabilityByCourtAndDate(court.getId(), date);
                //add number of slots
                totalSlotsCount += slots.size();
                //add number of slots that are not available
                takenSlotsCount += (int) slots.stream().filter(slot -> !slot.isAvailable()).count();
            }

            //calculate percentage
            double percentage = totalSlotsCount > 0 ? ((double) takenSlotsCount / totalSlotsCount) * 100.0 : 0.0;

            //create court report
            CourtOccupancyReport cor = new CourtOccupancyReport(
                court.getName(),
                takenSlotsCount,
                Math.round(percentage * 100.0) / 100.0
            );
            list.add(cor);
        }

        return list;
    }

    @Override
    public List<EquipmentTurnoverReport> getEquipmentTurnoverReport(int year, int month) {

        List<EquipmentTurnoverReport> list = new ArrayList<>();
        String query =  "SELECT e.name AS equipmentName, " +
                        "COALESCE(SUM(oi.quantity), 0) AS totalQuantitySold, " +
                        "COALESCE(SUM(oi.quantity * oi.priceAtPurchase), 0) AS totalRevenue " +
                        "FROM equipment e " +
                        "JOIN orderItem oi ON e.id = oi.equipmentId " +
                        "JOIN `Order` o ON oi.orderId = o.id " +
                        "WHERE YEAR(o.orderDate) = ? AND MONTH(o.orderDate) = ? AND o.status = 'PICKED_UP' " +
                        "GROUP BY e.id, e.name " +
                        "ORDER BY totalRevenue DESC";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ) {
            stm.setInt(1, year);
            stm.setInt(2, month);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                EquipmentTurnoverReport etr = new EquipmentTurnoverReport(
                    rs.getString("equipmentName"),
                    rs.getInt("totalQuantitySold"),
                    rs.getDouble("totalRevenue")
                );
                list.add(etr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
