package com.hub.backend.db.dao;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hub.backend.db.DB;
import com.hub.backend.models.Equipment;

public class EquipmentRepo implements EquipmentRepoInterface {

    @Override
    public List<Equipment> getEquipmentBySport(int sportId) {
        
        List<Equipment> equipment = new ArrayList<>();
        String query = "select e.*, s.name as sName from equipment e join sport s on s.id = e.sportId";
        if (sportId != 0) {
            query += " where s.id = ?";
        }

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            if (sportId != 0) {
                stm.setInt(1, sportId);
            }

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Equipment e = new Equipment(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("sportId"),
                    rs.getString("sName"),
                    rs.getFloat("price"),
                    rs.getInt("stock"),
                    rs.getString("imageUrl")
                );
                equipment.add(e);
            }
            
            return equipment;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return equipment;
    }

    @Override
    public boolean updatePriceStock(Equipment equipment) {
        
        String query = "update equipment set price = ?, stock = ? where id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setDouble(1, equipment.getPrice());
            stm.setInt(2, equipment.getStock());
            stm.setInt(3, equipment.getId());

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();    
        }

        return false;
    }

    @Override
    public boolean addEquipment(String name, int sportId, double price, int stock, MultipartFile file) {
        //image is required
        if (file == null || file.isEmpty()) {
            return false;
        }
        try {
            //setting up dir
            String uploadDir = "uploads/equipment/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //get file extension
            String originalFilename = file.getOriginalFilename();
            String extension = ".jpg";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            //generate unique name and save
            String fileName = name.replace(" ", "_") + extension;
            Path filePath = Paths.get(uploadDir + fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = uploadDir + fileName;

            //insert in db
            String query = "insert into equipment (name, sportId, price, stock, imageUrl) values (?, ?, ?, ?, ?)";
            try (Connection conn = DB.source().getConnection();
                 PreparedStatement stm = conn.prepareStatement(query)) {
                
                stm.setString(1, name);
                stm.setInt(2, sportId);
                stm.setDouble(3, price);
                stm.setInt(4, stock);
                stm.setString(5, imageUrl);

                return stm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
