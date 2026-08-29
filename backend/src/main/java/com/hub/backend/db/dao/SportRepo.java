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

    @Override
    public int getMaxMissingPlayers(int sportId) {
        
        String query = "select requiredPlayers from sport where id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, sportId);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()) return rs.getInt("requiredPlayers");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return 0;
    }

    @Override
    public List<Sport> getSportsByFacilityId(int facilityId) {
        
        List<Sport> sports = new ArrayList<>();
        String query =  "select distinct s.* from sport s join court c on c.sportId = s.id where c.facilityId = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, facilityId);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Sport sp = new Sport(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("requiredPlayers")
                );
                sports.add(sp);
            }

            return sports;
        } catch (Exception e) {
            e.printStackTrace();
        }
  
        return sports;
    }

    @Override
    public boolean addSport(Sport newSport) {
        
        String query = "insert into sport(name, requiredPlayers) values (?, ?)";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setString(1, newSport.getName());
            stm.setInt(2, newSport.getRequiredPlayers());

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
}
