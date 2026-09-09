package com.hub.backend.db.dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hub.backend.models.Equipment;

public interface EquipmentRepoInterface {
    
    List<Equipment> getEquipmentBySport(int sportId);
    boolean updatePriceStock(Equipment equipment);
    boolean addEquipment(String name, int sportId, double price, int stock, MultipartFile file);

}
