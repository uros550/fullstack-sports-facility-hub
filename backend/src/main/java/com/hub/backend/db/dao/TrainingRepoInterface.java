package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.Training;

public interface TrainingRepoInterface {
    
    List<Training> getAllTrainingsById(int athleteId);

}
