package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hub.backend.db.dao.TrainerRepo;
import com.hub.backend.models.SearchTrainerRequest;
import com.hub.backend.models.Trainer;

@RestController
@RequestMapping("/trainers")
@CrossOrigin(origins = "http://localhost:4200")
public class TrainerController {
    
    @GetMapping
    public List<Trainer> getAllTrainers() {
        return new TrainerRepo().getAllTrainers();
    }

    @GetMapping("/active")
    public List<Trainer> getAllActiveTrainers() {
        return new TrainerRepo().getAllActiveTrainers();
    }

    @PostMapping("/active/search")
    public List<Trainer> searchTrainers(@RequestBody SearchTrainerRequest request) {
        return new TrainerRepo().searchTrainers(request);
    }

    @PutMapping("/change/isActive")
    public boolean changeIsActive(@RequestParam int trainerId, @RequestParam boolean activate) {
        return new TrainerRepo().changeIsActive(trainerId, activate);
    }
    
}
