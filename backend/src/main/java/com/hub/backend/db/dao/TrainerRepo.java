package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
import com.hub.backend.models.SearchTrainerRequest;
import com.hub.backend.models.Trainer;

public class TrainerRepo implements TrainerRepoInterface {

    @Override
    public List<Trainer> getAllTrainers() {
        
        List<Trainer> trainers = new ArrayList<>();
        String query =  "select t.*, sf.name as sfName, s.name as sName from trainer t " + 
                        "join sportsfacility sf on sf.id = t.facilityId " + 
                        "join sport s on s.id = t.sportId";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Trainer t = new Trainer(
                    rs.getInt("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("specialization"),
                    rs.getFloat("rating"),
                    rs.getFloat("pricePerHour"),
                    rs.getInt("sportId"),
                    rs.getString("sName"),
                    rs.getInt("facilityId"),
                    rs.getString("sfName"),
                    rs.getBoolean("isActive")
                );
                trainers.add(t);
            }
            return trainers;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trainers;
    }

    @Override
    public List<Trainer> getAllActiveTrainers() {
        
        List<Trainer> trainers = new ArrayList<>();
        String query =  "select t.*, sf.name as sfName, s.name as sName from trainer t " + 
                        "join sportsfacility sf on sf.id = t.facilityId " + 
                        "join sport s on s.id = t.sportId " +
                        "where isActive = true";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Trainer t = new Trainer(
                    rs.getInt("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("specialization"),
                    rs.getFloat("rating"),
                    rs.getFloat("pricePerHour"),
                    rs.getInt("sportId"),
                    rs.getString("sName"),
                    rs.getInt("facilityId"),
                    rs.getString("sfName"),
                    rs.getBoolean("isActive")
                );
                trainers.add(t);
            }
            return trainers;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trainers;
    }

    @Override
    public List<Trainer> searchTrainers(SearchTrainerRequest request) {
        
        List<Trainer> trainers = new ArrayList<>();
        StringBuilder sql = new StringBuilder("select t.*, sf.name as sfName, s.name as sName from trainer t " + 
                            "join sportsfacility sf on sf.id = t.facilityId " + 
                            "join sport s on s.id = t.sportId " +
                            "where isActive = true");

        if (request.getFacilityId() > 0) {
            sql.append(" and sf.id = ?");
        }
        if (request.getSportId() > 0) {
            sql.append(" and s.id = ?");
        }

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql.toString());
        ){
            int paramIndex = 1;

            if (request.getFacilityId() > 0) {
                stm.setInt(paramIndex++, request.getFacilityId());
            }
            if (request.getSportId() > 0) {
                stm.setInt(paramIndex, request.getSportId());
            }

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Trainer t = new Trainer(
                    rs.getInt("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("specialization"),
                    rs.getFloat("rating"),
                    rs.getFloat("pricePerHour"),
                    rs.getInt("sportId"),
                    rs.getString("sName"),
                    rs.getInt("facilityId"),
                    rs.getString("sfName"),
                    rs.getBoolean("isActive")
                );
                trainers.add(t);
            }

            return trainers;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trainers;
    }

    @Override
    public boolean changeIsActive(int trainerId, boolean acitvate) {
        
        String query = "update trainer set isActive = ? where id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){  
            stm.setBoolean(1, acitvate);
            stm.setInt(2, trainerId);

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
  
}
