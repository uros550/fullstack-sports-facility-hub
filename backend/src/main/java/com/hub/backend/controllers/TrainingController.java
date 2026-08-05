package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hub.backend.db.dao.TrainingRepo;
import com.hub.backend.models.Training;

@RestController
@RequestMapping("/trainings")
@CrossOrigin(origins = "http://localhost:4200")
public class TrainingController {
    
    @GetMapping("/athlete/{athleteId}")
    public List<Training> getAllTrainingsById(@PathVariable int athleteId) {
        return new TrainingRepo().getAllTrainingsById(athleteId);
    }

}
