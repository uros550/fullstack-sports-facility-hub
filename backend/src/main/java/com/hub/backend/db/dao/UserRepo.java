package com.hub.backend.db.dao;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hub.backend.db.DB;
import com.hub.backend.models.AthleteProfile;
import com.hub.backend.models.User;

public class UserRepo implements UserRepoInterface {

    @Override
    public User login(User user) {

        String query = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
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
            PreparedStatement stmGetFacility = conn.prepareStatement(qGetFacility);
            PreparedStatement stmCountEmployee = conn.prepareStatement(qCountEmployee);
            PreparedStatement stmInsertUser = conn.prepareStatement(qInsertUser, Statement.RETURN_GENERATED_KEYS);      //get id
            PreparedStatement stmInsertFacEmp = conn.prepareStatement(qInsertFacEmp);
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
                stmGetFacility.setString(1, user.getFacilityMb());
                stmGetFacility.setString(2, user.getFacilityPib());
                rs = stmGetFacility.executeQuery();
                if (rs.next()) facilityId = rs.getInt("id");
                //exists
                if (facilityId != -1) {
                    //check if already 2 employees
                    stmCountEmployee.setInt(1, facilityId);
                    rs = stmCountEmployee.executeQuery();
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
                stmInsertFacEmp.setInt(1, facilityId);
                stmInsertFacEmp.setInt(2, generatedUserId);
                stmInsertFacEmp.executeUpdate();
            }

            //insert into userSport
            if ("ATHLETE".equals(user.getRole()) && user.getSports() != null) {
                for (Integer sportId : user.getSports()) {
                    stmInsertSport.setInt(1, generatedUserId);
                    stmInsertSport.setInt(2, sportId);
                    //doesn't require batch execution, max 5 updates
                    stmInsertSport.executeUpdate();
                }
            }

            conn.commit();
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String saveProfilePicture(String username, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return "File is empty";
        }

        try {
            //prepare dir
            String uploadDir = "uploads/profilePictures/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //get extension (jpg default)
            String originalFilename = file.getOriginalFilename();
            String extension = ".jpg";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            //generate unique filename
            String fileName = username + "_" + System.currentTimeMillis() + extension;
            Path filePath = Paths.get(uploadDir + fileName);
            //save file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            //update in database users profilePicture
            String ImagePath = "uploads/profilePictures/" + fileName;
            return updateProfilePicture(username, ImagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String updateProfilePicture(String username, String imagePath) {
        
        String query = "update user set profilePicture = ? where username = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setString(1, imagePath);
            stm.setString(2, username);

            int rows = stm.executeUpdate();

            if (rows > 0) {
                return "Success";
            } else {
                return "User not found";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public AthleteProfile getProfileById(int id) {
        
        String query = "select id, firstName, lastName, username, email, phone, profilePicture from user where id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                AthleteProfile profile = new AthleteProfile(
                    rs.getInt("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("profilePicture")
                );
                return profile;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Integer> getUserSportIds(int userId) {

        List<Integer> sportIds = new ArrayList<>();
        String query = "select sportId from usersport where userId = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, userId);

            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                sportIds.add(rs.getInt("sportId"));
            }
            return sportIds;
        } catch (Exception e) {
            e.printStackTrace();    
        }
        return sportIds;
    }

    @Override
    public String updateProfile(AthleteProfile newProfile) {

        String query = "update user set firstName = ?, lastName = ?, email = ?, phone = ? where id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setString(1, newProfile.getFirstName());
            stm.setString(2, newProfile.getLastName());
            stm.setString(3, newProfile.getEmail());
            stm.setString(4, newProfile.getPhone());
            stm.setInt(5, newProfile.getId());

            int rowsAffected = stm.executeUpdate();

            if (rowsAffected > 0) {
                return "Success";
            } else {
                return "User not found";
            }
        } catch (Exception e) {
            e.printStackTrace();    
        }
        return "";
    }

    @Override
    public String updateUserSportIds(int userId, List<Integer> newSportIds) {
        String query1 = "delete from usersport where userId = ?";
        String query2 = "insert into usersport(userId, sportId) values (?, ?)";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm1 = conn.prepareStatement(query1);
            PreparedStatement stm2 = conn.prepareStatement(query2);
        ){
            conn.setAutoCommit(false);

            stm1.setInt(1, userId);
            stm1.executeUpdate();

            if (newSportIds != null && !newSportIds.isEmpty()) {
                for (int sportId : newSportIds) {
                    stm2.setInt(1, userId);
                    stm2.setInt(2, sportId);
                    stm2.executeUpdate();
                }
            }
            
            conn.commit();
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();    
        }
        return "";
    }    
    
}
