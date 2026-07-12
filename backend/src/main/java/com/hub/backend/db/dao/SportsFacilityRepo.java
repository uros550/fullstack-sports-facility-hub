package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
import com.hub.backend.models.SportsFacility;

public class SportsFacilityRepo implements SportsFacilityRepoInterface {

    @Override
    public List<SportsFacility> getAllFacilites() {

        List<SportsFacility> facilities = new ArrayList<>();
        String query = "select * from sportsfacility";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                SportsFacility sf = new SportsFacility(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("city"),
                    rs.getString("mb"),
                    rs.getString("pib"),
                    rs.getString("description"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getString("workingHours"),
                    rs.getDouble("rating"),
                    rs.getInt("maxPenalties"),
                    rs.getString("status")
                );
                facilities.add(sf);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }


        return facilities;
    }
    
}
