package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hub.backend.db.dao.EquipmentRepo;
import com.hub.backend.models.Equipment;

@RestController
@RequestMapping("/equipment")
@CrossOrigin(origins = "http://localhost:4200")
public class EquipmentController {
    
    @GetMapping("/{sportId}")
    public List<Equipment> getEquipmentBySport(@PathVariable int sportId) {
        return new EquipmentRepo().getEquipmentBySport(sportId);
    }

    @PostMapping("/update")
    public boolean updatePriceStock(@RequestBody Equipment equipment) {
        return new EquipmentRepo().updatePriceStock(equipment);
    }
}
