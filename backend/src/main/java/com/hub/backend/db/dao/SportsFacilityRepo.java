package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hub.backend.db.DB;
import com.hub.backend.models.AvailabilitySlot;
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
    public List<SportsFacility> getFacilitiesForEmployee(int employeeId) {
        
        List<SportsFacility> facilities = new ArrayList<>();
        String query = "select sf.* from sportsFacility sf join facilityEmployee fe on sf.id = fe.facilityId where fe.employeeId = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, employeeId);

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
    public List<Court> getCourtsByFacilitySport(int facilityId, int sportId) {
        List<Court> courts = new ArrayList<>();
        String query =  "SELECT c.*, s.name AS sportName FROM court c JOIN sport s ON c.sportId = s.id " +
                        "WHERE c.facilityId = ? and c.sportId = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            
            stm.setInt(1, facilityId);
            stm.setInt(2, sportId);
            
            ResultSet rs = stm.executeQuery();
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
        StringBuilder sql = new StringBuilder("select distinct sf.* from sportsFacility sf join court c on sf.id = c.facilityId where sf.status = 'APPROVED'");

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
            
            //filter for free today only
            if (request.isFreeToday()) {
                LocalDate today = LocalDate.now();
                LocalTime now = LocalTime.now();
                ReservationRepo reservationRepo = new ReservationRepo();

                facilities.removeIf(sf -> {
                    List<Court> courts = getCourtsByFacilityId(sf.getId());
                    boolean hasAtLeastOneFreeSlot = false;

                    for (Court court : courts) {
                        if (request.getSportId() != null && request.getSportId() != 0 && court.getSportId() != request.getSportId()) {
                            continue; //check sport selected
                        }
                        if (request.getCourtType() != null && !request.getCourtType().isBlank() && !court.getType().equalsIgnoreCase(request.getCourtType())) {
                            continue; //check court type selected
                        }

                        //get slots
                        List<AvailabilitySlot> slots = reservationRepo.getAvailabilityByCourtAndDate(court.getId(), today);
                        
                        for (AvailabilitySlot slot : slots) {
                            //if any is available -> has at least one
                            LocalTime slotStartTime = LocalTime.parse(slot.getStartTime());

                            if (slot.isAvailable() && slotStartTime.isAfter(now)) { 
                                hasAtLeastOneFreeSlot = true;
                                break;
                            }
                        }
                        if (hasAtLeastOneFreeSlot) {
                            break; //found at least one break
                        }
                    }
                    //return true if none are available
                    return !hasAtLeastOneFreeSlot;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return facilities;
    }

    @Override
    public String addFacility(SportsFacility facility, List<Court> courts, int employeeId) {
        if (employeeId <= 0) {
            return "Error: employeeId not valid.";
        }

        if (courts == null || courts.isEmpty()) {
            return "Error: at least one court is required.";
        }

        Set<String> courtNames = new HashSet<>();
        for (Court court : courts) {
            if (court.getEquipmentDescription() != null && court.getEquipmentDescription().length() > 300) {
                return "Error: description on court '" + court.getName() + "' invalid.";
            }
            if (!courtNames.add(court.getName().trim().toLowerCase())) {
                return "Error: court names must be unique (Duplicate: '" + court.getName() + "').";
            }
        }

        String insertFacilityQuery = "INSERT INTO sportsfacility (name, address, city, mb, pib, description, latitude, longitude, workingHours, maxPenalties, likesCount, status) " +
                                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 'PENDING')";
        String insertCourtQuery = "INSERT INTO court (facilityId, sportId, name, type, capacity, equipmentDescription, pricePerHour) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection conn = DB.source().getConnection()
        ){
            conn.setAutoCommit(false);

            int facilityId = 0;
            try (PreparedStatement facStm = conn.prepareStatement(insertFacilityQuery, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                facStm.setString(1, facility.getName());
                facStm.setString(2, facility.getAddress());
                facStm.setString(3, facility.getCity());
                facStm.setString(4, facility.getMb());
                facStm.setString(5, facility.getPib());
                facStm.setString(6, facility.getDescription());
                facStm.setDouble(7, facility.getLatitude());
                facStm.setDouble(8, facility.getLongitude());
                facStm.setString(9, facility.getWorkingHours());
                facStm.setInt(10, facility.getMaxPenalties());

                int rows = facStm.executeUpdate();
                if (rows == 0) {
                    conn.rollback();
                    return "Error: Failed to insert sports facility.";
                }

                ResultSet rs = facStm.getGeneratedKeys();
                if (rs.next()) {
                    facilityId = rs.getInt(1);
                }
            }

            if (facilityId == 0) {
                conn.rollback();
                return "Error: Could not retrieve generated facility ID.";
            }

            boolean linked = linkEmployeeToFacility(conn, facilityId, employeeId);
            if (!linked) {
                conn.rollback();
                return "Error: Failed to link employee to facility.";
            }

            try (PreparedStatement courtStm = conn.prepareStatement(insertCourtQuery)) {
                for (Court court : courts) {
                    courtStm.setInt(1, facilityId);
                    courtStm.setInt(2, court.getSportId());
                    courtStm.setString(3, court.getName());
                    courtStm.setString(4, court.getType());
                    courtStm.setInt(5, court.getCapacity());
                    courtStm.setString(6, court.getEquipmentDescription());
                    courtStm.setDouble(7, court.getPricePerHour());
                    if (courtStm.executeUpdate() == 0) {
                        conn.rollback();
                        return "Error: Failed to insert a court.";
                    };
                }
            }

            conn.commit();
            return "SUCCESS";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public boolean linkEmployeeToFacility(Connection conn, int facilityId, int employeeId) {

        String query = "INSERT INTO facilityemployee (facilityId, employeeId) VALUES (?, ?)";

        try (
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, facilityId);
            stm.setInt(2, employeeId);

            return stm.executeUpdate() > 0; 
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateFacility(SportsFacility facility) {

        String query = "UPDATE sportsfacility SET name = ?, address = ?, city = ?, mb = ?, pib = ?, description = ?, " +
                       "latitude = ?, longitude = ?, workingHours = ?, maxPenalties = ? WHERE id = ?";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(query)) {

            stm.setString(1, facility.getName());
            stm.setString(2, facility.getAddress());
            stm.setString(3, facility.getCity());
            stm.setString(4, facility.getMb());
            stm.setString(5, facility.getPib());
            stm.setString(6, facility.getDescription());
            stm.setDouble(7, facility.getLatitude());
            stm.setDouble(8, facility.getLongitude());
            stm.setString(9, facility.getWorkingHours());
            stm.setInt(10, facility.getMaxPenalties());
            stm.setInt(11, facility.getId());

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean addCourt(Court court) {
        if (court.getEquipmentDescription() != null && court.getEquipmentDescription().length() > 300) {
            return false;
        }
        //check unique name
        if (!checkCourtNameUnique(court.getFacilityId(), court.getName())) {
            return false;
        }

        String query = "INSERT INTO court (facilityId, sportId, name, type, capacity, equipmentDescription, pricePerHour) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query)
        ){
            stm.setInt(1, court.getFacilityId());
            stm.setInt(2, court.getSportId());
            stm.setString(3, court.getName());
            stm.setString(4, court.getType());
            stm.setInt(5, court.getCapacity());
            stm.setString(6, court.getEquipmentDescription());
            stm.setDouble(7, court.getPricePerHour());

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    @Override
    public boolean checkCourtNameUnique(int facilityId, String courtName) {
        if (courtName == null || courtName.trim().isEmpty()) {
            return false;
        }

        String query = "SELECT COUNT(*) FROM court WHERE facilityId = ? AND LOWER(TRIM(name)) = LOWER(TRIM(?))";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query)
        ){
            stm.setInt(1, facilityId);
            stm.setString(2, courtName);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;    
    }
     
}
