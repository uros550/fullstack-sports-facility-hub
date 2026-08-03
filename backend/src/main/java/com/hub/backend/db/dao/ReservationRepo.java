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
                       "WHERE r.athleteId = ? AND r.status = 'CONFIRMED'" + //DODAJ START TIME > NOW()
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

            int rowsAffected = stm.executeUpdate();
            if (rowsAffected > 0) {
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

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement workingHoursStm = conn.prepareStatement(workHoursQuery);
            PreparedStatement reservationStm = conn.prepareStatement(reservationsQuery);
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

            //get reserved start and end times
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
    
}
