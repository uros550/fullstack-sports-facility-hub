package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
import com.hub.backend.models.Reservation;

public class ReservationRepo implements ReservationRepoInterface {

    @Override
    public List<Reservation> getReservationsByAthleteId(int athleteId) {

        List<Reservation> allReservations = new ArrayList<>();
        String query = "SELECT r.id, r.facilityId, sf.name AS facilityName, sf.city, r.courtId, c.name AS courtName, " +
                       "r.athleteId, r.sportId, s.name AS sportName, r.startTime, r.endTime, r.status, r.missingPlayers " +
                       "FROM reservation r " +
                       "JOIN sportsfacility sf ON sf.id = r.facilityId " +
                       "JOIN court c ON c.id = r.courtId " +
                       "JOIN sport s ON s.id = r.sportId " +
                       "WHERE r.athleteId = ? " +
                       "ORDER BY r.startTime DESC";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ) {
            stm.setInt(1, athleteId);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {

                Timestamp startTs = rs.getTimestamp("startTime");
                LocalDateTime startDate = (startTs != null) ? startTs.toLocalDateTime() : null;
                
                Timestamp endTs = rs.getTimestamp("endTime");
                LocalDateTime endDate = (endTs != null) ? endTs.toLocalDateTime() : null;
                
                Reservation r = new Reservation(
                    rs.getInt("id"),
                    rs.getInt("facilityId"),
                    rs.getString("facilityName"),
                    rs.getString("city"),
                    rs.getInt("courtId"),
                    rs.getString("courtName"),
                    rs.getInt("athleteId"),
                    rs.getInt("sportId"),
                    rs.getString("sportName"),
                    startDate,
                    endDate,
                    rs.getString("status"),
                    rs.getInt("missingPlayers")
                );
                allReservations.add(r);
            }
            return allReservations;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allReservations;
    }
    
}
