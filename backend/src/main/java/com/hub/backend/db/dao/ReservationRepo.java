package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
import com.hub.backend.models.Application;
import com.hub.backend.models.AvailabilitySlot;
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

    @Override
    public List<Reservation> getActiveReservationsByAthleteId(int athleteId) {

        List<Reservation> allReservations = new ArrayList<>();
        String query = "SELECT r.id, r.facilityId, sf.name AS facilityName, sf.city, r.courtId, c.name AS courtName, " +
                       "r.athleteId, r.sportId, s.name AS sportName, r.startTime, r.endTime, r.status, r.missingPlayers " +
                       "FROM reservation r " +
                       "JOIN sportsfacility sf ON sf.id = r.facilityId " +
                       "JOIN court c ON c.id = r.courtId " +
                       "JOIN sport s ON s.id = r.sportId " +
                       "WHERE r.athleteId = ? AND r.status = 'CONFIRMED' AND r.endTime > NOW() " +
                       "ORDER BY r.startTime ASC";

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

    @Override
    public List<Reservation> getActiveReservationsByEmployeeId(int employeeId) {

        List<Reservation> allReservations = new ArrayList<>();
        String query = "SELECT r.id, r.facilityId, sf.name AS facilityName, sf.city, r.courtId, c.name AS courtName, " +
                       "r.athleteId, r.sportId, s.name AS sportName, r.startTime, r.endTime, r.status, r.missingPlayers " +
                       "FROM reservation r " +
                       "JOIN sportsfacility sf ON sf.id = r.facilityId " +
                       "JOIN court c ON c.id = r.courtId " +
                       "JOIN sport s ON s.id = r.sportId " +
                       "JOIN facilityemployee fe ON fe.facilityId = sf.id " +
                       "WHERE r.status IN ('CONFIRMED', 'PENDING') AND r.startTime > NOW() - INTERVAL 10 MINUTE AND fe.employeeId = ? " +
                       "ORDER BY r.startTime ASC";
        
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ) {
            stm.setInt(1, employeeId);

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

    @Override
    public boolean acceptReservation(int reservationId) {

        String query = "update reservation set status = 'CONFIRMED' where id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query)
        ) {
            stm.setInt(1, reservationId);
            return stm.executeUpdate() > 0; //true if affected
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rejectReservation(int reservationId) {

        String query = "update reservation set status = 'CANCELLED' where id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query)
        ) {
            stm.setInt(1, reservationId);
            return stm.executeUpdate() > 0; //true if affected
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cancelReservation(int reservationId) {
        //update only if more than 12hours
        String query = "update reservation set status = 'CANCELLED' where id = ? and startTime >= NOW() + INTERVAL 12 HOUR";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query)
        ) {
            stm.setInt(1, reservationId);
            return stm.executeUpdate() > 0; //true if affected
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean noShowReservation(int reservationId, int athleteId, int facilityId) {

        String noShowQuery = "update reservation set status = 'DIDNT_SHOW' where id = ?";
        String checkQuery = "select count(*) as count from reservation where athleteId = ? and facilityId = ? and status = 'DIDNT_SHOW'";
        String maxQuery = "select maxPenalties from sportsfacility where id = ?";
        String blockQuery = "insert into facilityblock values (?, ?)";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement noShowStm = conn.prepareStatement(noShowQuery);
            PreparedStatement checkStm = conn.prepareStatement(checkQuery);
            PreparedStatement maxStm = conn.prepareStatement(maxQuery);
            PreparedStatement blockStm = conn.prepareStatement(blockQuery);
        ){
            conn.setAutoCommit(false);

            noShowStm.setInt(1, reservationId);
            if (noShowStm.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }

            checkStm.setInt(1, athleteId);
            checkStm.setInt(2, facilityId);
            maxStm.setInt(1, facilityId);
            
            int count = 0;
            int max = 0;
            ResultSet countRs = checkStm.executeQuery();
            ResultSet maxRs = maxStm.executeQuery();
            if (countRs.next()) count = countRs.getInt("count");
            if (maxRs.next()) max = maxRs.getInt("maxPenalties");
            if (count > max && max > 0) {
                blockStm.setInt(1, facilityId);
                blockStm.setInt(2, athleteId);
                if (blockStm.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String addReservation(Reservation newReservation) {
        
        String query = "insert into reservation (facilityId, courtId, athleteId, sportId, startTime, endTime, status, missingPlayers) values (?, ?, ?, ?, ?, ?, 'PENDING', ?)";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, newReservation.getFacilityId());
            stm.setInt(2, newReservation.getCourtId());
            stm.setInt(3, newReservation.getAthleteId());
            stm.setInt(4, newReservation.getSportId());
            stm.setTimestamp(5, Timestamp.valueOf(newReservation.getStartTime()));
            stm.setTimestamp(6, Timestamp.valueOf(newReservation.getEndTime()));
            stm.setInt(7, newReservation.getMissingPlayers());
            
            //ovde ce biti samo check da li je facility block koji vraca Blocked

            if (stm.executeUpdate() > 0) {
                return "Success";
            }
            else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "";
    }

    @Override
    public List<AvailabilitySlot> getAvailabilityByCourtAndDate(int courtId, LocalDate date) {
        
        List<AvailabilitySlot> slots = new ArrayList<>();
        List<LocalTime[]> reservedTimes = new ArrayList<>();
        String workHoursQuery = "select sf.workingHours from court c join sportsfacility sf on sf.id = c.facilityId where c.id = ?";
        String reservationsQuery = "select startTime, endTime from reservation where courtId = ? and DATE(startTime) = ? and status in ('PENDING', 'CONFIRMED')";
        String trainingsQuery = "select startTime, endTime from training where courtId = ? and DATE(startTime) = ? and status in ('PENDING', 'CONFIRMED')";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement workingHoursStm = conn.prepareStatement(workHoursQuery);
            PreparedStatement reservationStm = conn.prepareStatement(reservationsQuery);
            PreparedStatement trainingStm = conn.prepareStatement(trainingsQuery);
        ){
            //get facility working hours
            workingHoursStm.setInt(1, courtId);

            ResultSet workingHoursRs = workingHoursStm.executeQuery();
            if (!workingHoursRs.next()) {
                return slots;
            }
            //get start and end time 10:00 - 22:00 (working hours)
            String workingHours = workingHoursRs.getString("workingHours");
            if (workingHours == null || workingHours.isBlank()) {
                return slots;
            }
            String[] hours = workingHours.split(" - ");
            LocalTime startWorkingTime = LocalTime.parse(hours[0]); //10:00
            LocalTime endWorkingTime = LocalTime.parse(hours[1]);   //22:00

            //get reserved start and end times from reservations
            reservationStm.setInt(1, courtId);
            reservationStm.setDate(2, java.sql.Date.valueOf(date));

            ResultSet reservationsRs = reservationStm.executeQuery();
            while (reservationsRs.next()) {
                Timestamp startTimestamp = reservationsRs.getTimestamp("startTime");
                Timestamp endTimestamp = reservationsRs.getTimestamp("endTime");

                if (startTimestamp != null && endTimestamp != null) {
                    LocalTime reservationStart = startTimestamp.toLocalDateTime().toLocalTime();
                    LocalTime reservationEnd = endTimestamp.toLocalDateTime().toLocalTime();
                    //add into reserved times array
                    reservedTimes.add(new LocalTime[] {
                        reservationStart,
                        reservationEnd
                    });
                }
            }

            //get reserved start and end times from trainings
            trainingStm.setInt(1, courtId);
            trainingStm.setDate(2, java.sql.Date.valueOf(date));

            ResultSet trainingsRs = trainingStm.executeQuery();
            while (trainingsRs.next()) {
                Timestamp startTimestamp = trainingsRs.getTimestamp("startTime");
                Timestamp endTimestamp = trainingsRs.getTimestamp("endTime");

                if (startTimestamp != null && endTimestamp != null) {
                    LocalTime trainingStart = startTimestamp.toLocalDateTime().toLocalTime();
                    LocalTime trainingEnd = endTimestamp.toLocalDateTime().toLocalTime();
                    //add into reserved times array
                    reservedTimes.add(new LocalTime[] {
                        trainingStart,
                        trainingEnd
                    });
                }
            }

            //create hourly slots
            LocalTime currentTime = startWorkingTime;
            //go through working hours
            while (currentTime.isBefore(endWorkingTime)) {
                LocalTime slotEnd = currentTime.plusHours(1);
                //check if this slot is reserved
                boolean isAvailable = true;
                for (LocalTime[] reservation : reservedTimes) {
                    LocalTime reservationStart = reservation[0];
                    LocalTime reservationEnd = reservation[1];
                    //check if slot overlaps any reservation
                    if (currentTime.isBefore(reservationEnd) && slotEnd.isAfter(reservationStart)) {
                        isAvailable = false;
                        break;
                    }
                }
                //actual slot with availability info
                slots.add(new AvailabilitySlot(
                    currentTime.toString(),
                    slotEnd.toString(),
                    isAvailable
                ));
                //go next
                currentTime = slotEnd;
            }
            
            return slots;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return slots;
    }

    @Override
    public int updateMissingPlayers(int missingPlayer, int reservationId) {
        
        String query = "update reservation set missingPlayers = ? where id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, missingPlayer);
            stm.setInt(2, reservationId);
            
            return stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public List<Reservation> getReservationAds(int athleteId) {
        
        List<Reservation> allReservations = new ArrayList<>();
        String query = "SELECT r.id, r.facilityId, sf.name AS facilityName, sf.city, r.courtId, c.name AS courtName, " +
                       "r.athleteId, r.sportId, s.name AS sportName, r.startTime, r.endTime, r.status, r.missingPlayers " +
                       "FROM reservation r " +
                       "JOIN sportsfacility sf ON sf.id = r.facilityId " +
                       "JOIN court c ON c.id = r.courtId " +
                       "JOIN sport s ON s.id = r.sportId " +
                       "WHERE r.athleteId <> ? AND r.status = 'CONFIRMED' and r.missingPlayers > 0 " + //DODAJ START TIME > NOW()
                       "ORDER BY r.startTime ASC";

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

    @Override
    public List<Application> getAllApplicationsForAthlete(int athleteId) {
        
        List<Application> applications = new ArrayList<>();
        String query = "select * from joinrequest where athleteId = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, athleteId);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Application app = new Application(
                    rs.getInt("id"),
                    rs.getInt("reservationId"),
                    rs.getInt("athleteId"),
                    rs.getString("status")
                );
                applications.add(app);
            }
            return applications;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return applications;
    }

    @Override
    public boolean applyToAd(int reservationId, int athleteId) {
        
        String query = "insert into joinrequest (reservationId, athleteId, status) values (?, ?, 'PENDING')";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, reservationId);
            stm.setInt(2, athleteId);

            return stm.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean acceptRejectApp(int reservationId, int athleteId, boolean accept) {
        
        String subtractPlayersQuery = "update reservation set missingPlayers = missingPlayers - 1 where id = ?"; 
        String changeStatusQuery = "update joinrequest set status = ? where reservationId = ? and athleteId = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement subtractStm = conn.prepareStatement(subtractPlayersQuery);
            PreparedStatement changeStm = conn.prepareStatement(changeStatusQuery);
        ){
            conn.setAutoCommit(false);

            changeStm.setString(1, accept ? "ACCEPTED" : "REJECTED");
            changeStm.setInt(2, reservationId);
            changeStm.setInt(3, athleteId);

            //this executes anyways
            int changeRows = changeStm.executeUpdate();
            if (changeRows == 0) {
                conn.rollback();
                return false;
            }

            //if accepted subtract 1 missing player
            if (accept) {
                subtractStm.setInt(1, reservationId);
                int subtractRows = subtractStm.executeUpdate();
                if (subtractRows == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Application> getAllApplicationsByAthlete(int athleteId) {
        
        List<Application> allApplications = new ArrayList<>();
        String query = "select jr.* from joinrequest jr join reservation r on jr.reservationId = r.id where r.athleteId = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, athleteId);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Application app = new Application(
                    rs.getInt("id"),
                    rs.getInt("reservationId"),
                    rs.getInt("athleteId"),
                    rs.getString("status")
                );
                allApplications.add(app);
            }
            return allApplications;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allApplications;
    }
    
}
