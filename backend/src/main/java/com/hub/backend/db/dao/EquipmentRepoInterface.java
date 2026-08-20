package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.Equipment;

public interface EquipmentRepoInterface {
    
    List<Equipment> getEquipmentBySport(int sportId);
    boolean updatePriceStock(Equipment equipment);

}
