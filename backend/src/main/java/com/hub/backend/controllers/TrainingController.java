package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/employee/{employeeId}")
    public List<Training> getActiveTrainingsByEmployeeId(@PathVariable int employeeId) {
        return new TrainingRepo().getActiveTrainingsByEmployeeId(employeeId);
    }

    @PostMapping("/create")
    public String reserveTraining(@RequestBody Training newTraining) {
        return new TrainingRepo().reserveTraining(newTraining);
    }

}
