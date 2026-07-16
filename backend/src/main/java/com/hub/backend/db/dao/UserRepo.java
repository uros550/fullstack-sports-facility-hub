package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


import com.hub.backend.db.DB;
import com.hub.backend.models.User;

public class UserRepo implements UserRepoInterface {

    @Override
    public User login(User user) {

        String query = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ) {
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getPassword());
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                User u = new User(
                    rs.getInt("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("profilePicture"),
                    rs.getString("role"),
                    rs.getString("status")
                );
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String register(User user) {
        //queries 
        String qCheckUser = "select 1 from user where username = ?";
        String qCheckEmail = "select 1 from user where email = ?";
        String qGetFacility = "select id from sportsfacility where mb = ? or pib = ?";
        String qCountEmployee = "select count(*) from facilityemployee fe join user u on fe.employeeId = u.id where fe.facilityId = ? and u.status != 'REJECTED'";
        String qInsertUser   = "insert into user (firstName, lastName, username, email, password, phone, profilePicture, role, status) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String qInsertFacEmp = "insert into facilityemployee (facilityId, employeeId) values (?, ?)";
        String qInsertSport = "insert into usersport (userId, sportId) values (?, ?)";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stmCheckUser  = conn.prepareStatement(qCheckUser);
            PreparedStatement stmCheckEmail = conn.prepareStatement(qCheckEmail);
            PreparedStatement stmGetFac     = conn.prepareStatement(qGetFacility);
            PreparedStatement stmCountEmp   = conn.prepareStatement(qCountEmployee);
            PreparedStatement stmInsertUser = conn.prepareStatement(qInsertUser, Statement.RETURN_GENERATED_KEYS);      //get id
            PreparedStatement stmInsertRel  = conn.prepareStatement(qInsertFacEmp);
            PreparedStatement stmInsertSport = conn.prepareStatement(qInsertSport);
        ){
            conn.setAutoCommit(false);

            //username check
            stmCheckUser.setString(1, user.getUsername());
            ResultSet rs = stmCheckUser.executeQuery();
            if (rs.next()) return "Username already exists.";

            //email check
            stmCheckEmail.setString(1, user.getEmail());
            rs = stmCheckEmail.executeQuery();
            if (rs.next()) return "Email already exists.";

            int facilityId = -1;

            //employee register
            if ("EMPLOYEE".equals(user.getRole())) {
                if (user.getFacilityMb() == null || !user.getFacilityMb().matches("^\\d{8}$")) {
                    return "MB wrong format.";
                }
                if (user.getFacilityPib() == null || !user.getFacilityPib().matches("^[1-9]\\d{8}$")) {
                    return "PIB wrong format.";
                }

                //check facility exists
                stmGetFac.setString(1, user.getFacilityMb());
                stmGetFac.setString(2, user.getFacilityPib());
                rs = stmGetFac.executeQuery();
                if (rs.next()) facilityId = rs.getInt("id");
                //exists
                if (facilityId != -1) {
                    //check if already 2 employees
                    stmCountEmp.setInt(1, facilityId);
                    rs = stmCountEmp.executeQuery();
                    if (rs.next() && rs.getInt(1) >= 2) {
                        return "Already maximum number of employees.";
                    }
                }
                else {
                    return "Facility doesn't exist.";
                }
            }

            if ("ATHLETE".equals(user.getRole()) && user.getSports() != null) {
                 if (user.getSports().size() > 5) {
                    return "Maximum of 5 sports.";
                }
            }
            
            //pictures
            user.setProfilePicture("C:\\Users\\uros550\\Pictures\\profilePictures\\default.jpg");

            //insert new user pending
            int generatedUserId = -1;
            stmInsertUser.setString(1, user.getFirstName());
            stmInsertUser.setString(2, user.getLastName());
            stmInsertUser.setString(3, user.getUsername());
            stmInsertUser.setString(4, user.getEmail());
            stmInsertUser.setString(5, user.getPassword());
            stmInsertUser.setString(6, user.getPhone());
            stmInsertUser.setString(7, user.getProfilePicture());
            stmInsertUser.setString(8, user.getRole());
            stmInsertUser.setString(9, "PENDING");

            stmInsertUser.executeUpdate();
            rs = stmInsertUser.getGeneratedKeys();
            if (rs.next()) {
                generatedUserId = rs.getInt(1); //get userId autoincrement
            }
            if (generatedUserId == -1) return "Created user, but database didn't return id";

            //insert into facilityEmployee
            if ("EMPLOYEE".equals(user.getRole())) {
                stmInsertRel.setInt(1, facilityId);
                stmInsertRel.setInt(2, generatedUserId);
                stmInsertRel.executeUpdate();
            }

            //insert into userSport
            if ("ATHLETE".equals(user.getRole()) && user.getSports() != null) {
                for (Integer sportId : user.getSports()) {
                    stmInsertSport.setInt(1, generatedUserId);
                    stmInsertSport.setInt(2, sportId);
                    stmInsertSport.addBatch();
                }
                stmInsertSport.executeBatch();
            }

            conn.commit();
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Success";
    }    
}
