package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
import com.hub.backend.models.User;

public class AdminRepo implements AdminRepoInterface {

    @Override
    public List<User> getAllUsers() {
        
        List<User> users = new ArrayList<>();
        String query = "select * from user where role <> 'ADMIN'";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
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
                users.add(u);
                //maybe later set sports/facility for athlete/employee
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public List<User> getPendingUsers() {
        
        List<User> users = new ArrayList<>();
        String query = "select * from user where role <> 'ADMIN' and status = 'PENDING'";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
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
                users.add(u);
                //maybe later set sports/facility for athlete/employee
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public boolean changeUsername(int userId, String newUsername) {
        
        String query = "update user set username = ? where id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setString(1, newUsername);
            stm.setInt(2, userId);

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean acceptRegistration(int userId) {
        
        String query = "update user set status = 'APPROVED' where id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, userId);
            
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
     @Override
    public boolean rejectRegistration(int userId) {
        
        String query = "update user set status = 'REJECTED' where id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, userId);
            
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    @Override
    public boolean deleteAccount(int userId) {
        
        String query = "update user set status = 'REJECTED' where id = ?"; //maybe change later in db 'DELETED'

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, userId);
            
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
}
