package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.Promotion;

public interface PromotionRepoInterface {
    
    public List<Promotion> getActivePromotions();
    public List<Promotion> getPromotionsByEmployeeId(int employeeId);
    public boolean createPromotion(Promotion newPromotion);
    public boolean updatePromotion(Promotion newPromotion);
    public Promotion getCurrentPromotion(int facilityId, int sportId);

}
