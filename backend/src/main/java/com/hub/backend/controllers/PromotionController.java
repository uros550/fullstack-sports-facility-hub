package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hub.backend.db.dao.PromotionRepo;
import com.hub.backend.models.Promotion;

@RestController
@RequestMapping("/promotions")
@CrossOrigin(origins = "http://localhost:4200")
public class PromotionController {

    @GetMapping("/active")
    public List<Promotion> getActivePromotions() {
        return new PromotionRepo().getActivePromotions();
    }

    @GetMapping("/employee/{employeeId}")
    public List<Promotion> getPromotionsByEmployeeId(@PathVariable int employeeId) {
        return new PromotionRepo().getPromotionsByEmployeeId(employeeId);
    }

    @PostMapping("/create")
    public String createPromotion(@RequestBody Promotion promotion) {
        boolean success = new PromotionRepo().createPromotion(promotion);
        return success ? "Success" : "Error";
    }

    @PutMapping("/update")
    public String updatePromotion(@RequestBody Promotion promotion) {
        boolean success = new PromotionRepo().updatePromotion(promotion);
        return success ? "Success" : "Error";
    }
    
}
