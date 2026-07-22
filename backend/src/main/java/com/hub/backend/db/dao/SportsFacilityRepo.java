package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
import com.hub.backend.models.Court;
import com.hub.backend.models.SearchFacilitiesRequest;
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
                    rs.getInt("maxPenalties"),
                    rs.getInt("likesCount"),
                    rs.getString("status")
                );
                facilities.add(sf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return facilities;
    }

    @Override
    public List<SportsFacility> getAllActiveFacilities() {
        
        List<SportsFacility> facilities = new ArrayList<>();
        String query = "select * from sportsfacility where status = 'APPROVED'";

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
                    rs.getInt("maxPenalties"),
                    rs.getInt("likesCount"),
                    rs.getString("status")
                );
                facilities.add(sf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return facilities;
    }

    @Override
    public List<SportsFacility> getTop3Facilities() {
        
        List<SportsFacility> top3Facilities = new ArrayList<>();
        String query = "select * from sportsfacility where status='APPROVED' order by likesCount DESC limit 3";

        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ) {
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
                    rs.getInt("maxPenalties"),
                    rs.getInt("likesCount"),
                    rs.getString("status")
                );
                top3Facilities.add(sf);
            } 
        } catch (Exception e) {
                e.printStackTrace();
        }

        return top3Facilities;
    }

    @Override
    public SportsFacility getFacilityById(int id) {
        
        String query = "select * from sportsFacility where id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
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
                    rs.getInt("maxPenalties"),
                    rs.getInt("likesCount"),
                    rs.getString("status")
                );
                return sf;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getActiveFacilitiesCount() {

        int count = 0;
        String query = "select count(*) from sportsfacility where status = 'APPROVED'";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }

        return count;
    }

    @Override
    public List<Court> getCourtsByFacilityId(int facilityId) {
        
        List<Court> courts = new ArrayList<>();
        String query = "SELECT c.*, s.name AS sportName FROM court c LEFT JOIN sport s ON c.sportId = s.id WHERE c.facilityId = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ){
            
            stmt.setInt(1, facilityId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Court court = new Court(
                    rs.getInt("id"),
                    rs.getInt("facilityId"),
                    rs.getInt("sportId"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getInt("capacity"),
                    rs.getString("equipmentDescription"),
                    rs.getDouble("pricePerHour"),
                    rs.getString("sportName") //sport name for details
                );
                courts.add(court);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return courts;
    }

    @Override
    public List<String> getImagesByFacilityId(int facilityId) {
        
        List<String> images = new ArrayList<>();
        String query = "select imageUrl from facilityImage where facilityId = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, facilityId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                images.add(rs.getString("imageUrl"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }  

    @Override
    public List<String> getAllCities() {
        
        List<String> allCities = new ArrayList<>();
        String query = "select distinct city from sportsFacility where status = 'APPROVED'";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                allCities.add(rs.getString("city"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allCities;
    }

    @Override
    public List<SportsFacility> searchFacilities(SearchFacilitiesRequest request) {
        
        List<SportsFacility> facilities = new ArrayList<>();
        StringBuilder sql = new StringBuilder("select distinct sf.* from sportsFacility sf left join court c on sf.id = c.facilityId where sf.status = 'approved'");

        //name check
        if (request.getName() != null && !request.getName().isBlank()) {
            sql.append(" and lower(sf.name) like ?");
        }

        //sport check
        if (request.getSportId() != null && request.getSportId() != 0) {
            sql.append(" and c.sportId = ?");
        }

        //type check
        if (request.getCourtType() != null && !request.getCourtType().isBlank()) {
            sql.append(" and c.type = ?");
        }

        //city check
        if (request.getCities() != null && !request.getCities().isEmpty()) {
            sql.append(" and sf.city in (");
            for (int i = 0; i < request.getCities().size(); i++) {
                sql.append("?");
                if (i < request.getCities().size() - 1) {
                    sql.append(", ");
                }
            }
            sql.append(")");
        }

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql.toString());
        ) {
            int paramIndex = 1;

            //name check
            if (request.getName() != null && !request.getName().isBlank()) {
                stm.setString(paramIndex++, "%" + request.getName().toLowerCase() + "%");
            }

            //sport check
            if (request.getSportId() != null && request.getSportId() != 0) {
                stm.setInt(paramIndex++, request.getSportId());
            }

            //type check
            if (request.getCourtType() != null && !request.getCourtType().isBlank()) {
                stm.setString(paramIndex++, request.getCourtType());
            }

            //city check
            if (request.getCities() != null && !request.getCities().isEmpty()) {
                for (String city : request.getCities()) {
                    stm.setString(paramIndex++, city);
                }
            }

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
                    rs.getInt("maxPenalties"),
                    rs.getInt("likesCount"),
                    rs.getString("status")
                );
                facilities.add(sf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return facilities;
    }
     
}
