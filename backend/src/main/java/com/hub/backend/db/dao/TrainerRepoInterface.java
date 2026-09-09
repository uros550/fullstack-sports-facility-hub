package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.SearchTrainerRequest;
import com.hub.backend.models.Trainer;

public interface TrainerRepoInterface {
    
    List<Trainer> getAllTrainers();
    List<Trainer> getAllActiveTrainers();
    List<Trainer> searchTrainers(SearchTrainerRequest request);
    boolean changeIsActive(int trainerId, boolean activate);

}
