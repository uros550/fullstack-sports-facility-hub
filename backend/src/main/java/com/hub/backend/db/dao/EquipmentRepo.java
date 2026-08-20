package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
    
}
