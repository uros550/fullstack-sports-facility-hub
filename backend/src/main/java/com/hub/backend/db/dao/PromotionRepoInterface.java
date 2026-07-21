package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.Promotion;

public interface PromotionRepoInterface {
    
    public List<Promotion> getActivePromotions();

}
