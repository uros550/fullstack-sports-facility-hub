package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
import com.hub.backend.models.Training;

public class TrainingRepo implements TrainingRepoInterface {

    @Override
    public List<Training> getAllTrainingsById(int athleteId) {
        
        List<Training> trainings = new ArrayList<>();
        String query = "SELECT tr.*, CONCAT(t.firstName, ' ', t.lastName) AS trainerName, " +
                       "u.username AS athleteUsername, sf.name AS facilityName, c.name AS courtName " +
                       "FROM training tr " +
                       "JOIN trainer t ON t.id = tr.trainerId " +
                       "JOIN user u ON u.id = tr.athleteId " +
                       "JOIN sportsfacility sf ON sf.id = tr.facilityId " +
                       "JOIN court c ON c.id = tr.courtId " +
                       "WHERE tr.athleteId = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, athleteId);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Timestamp startTs = rs.getTimestamp("startTime");
                LocalDateTime startDate = (startTs != null) ? startTs.toLocalDateTime() : null;
                
                Timestamp endTs = rs.getTimestamp("endTime");
                LocalDateTime endDate = (endTs != null) ? endTs.toLocalDateTime() : null;

                String status = rs.getString("status");
                if ("CONFIRMED".equals(status) && endDate != null && LocalDateTime.now().isAfter(endDate)) {
                    status = "COMPLETED";
                }

                Training tr = new Training(
                    rs.getInt("id"),
                    rs.getInt("trainerId"),
                    rs.getString("trainerName"),
                    rs.getInt("athleteId"),
                    rs.getString("athleteUsername"),
                    rs.getInt("facilityId"),
                    rs.getString("facilityName"),
                    rs.getInt("courtId"),
                    rs.getString("courtName"),
                    startDate,
                    endDate,
                    rs.getDouble("price"),
                    status
                );
                trainings.add(tr);
            }

            return trainings;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trainings;
    }
    
}
