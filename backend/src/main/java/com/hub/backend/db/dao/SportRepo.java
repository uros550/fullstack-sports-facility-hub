package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
import com.hub.backend.models.Sport;

public class SportRepo implements SportRepoInterface {

    @Override
    public List<Sport> getAllSports() {
        
        List<Sport> allSports = new ArrayList<>();
        String query = "select * from sport order by name";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Sport sp = new Sport(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("requiredPlayers")
                );
                allSports.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allSports;

    }
    
}
