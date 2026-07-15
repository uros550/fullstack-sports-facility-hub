package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
                return new User(
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }    
}
