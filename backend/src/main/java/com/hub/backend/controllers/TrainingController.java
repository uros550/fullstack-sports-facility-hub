package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/accept/{id}")
    public String acceptTraining(@PathVariable int id) {
        boolean success = new TrainingRepo().acceptTraining(id);
        return success ? "Success" : "Error";
    }

    @PostMapping("/reject/{id}")
    public String rejectTraining(@PathVariable int id) {
        boolean success = new TrainingRepo().rejectTraining(id);
        return success ? "Success" : "Error";
    }

    @PostMapping("/noShow/{id}")
    public String noShowTraining(@PathVariable int id, @RequestParam int athleteId, @RequestParam int facilityId) {  
        boolean success = new TrainingRepo().noShowTraining(id, athleteId, facilityId);
        return success ? "Success" : "Error";
    }

}
