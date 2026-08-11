package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
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
        
}
