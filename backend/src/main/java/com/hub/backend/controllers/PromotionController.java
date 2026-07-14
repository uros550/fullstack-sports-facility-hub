package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
}
