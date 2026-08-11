package com.hub.backend.models;

public class MonthlyActivity {
    
    private String month;
    private int activityCount;

    //constructors
    public MonthlyActivity() {}

    public MonthlyActivity(String month, int activityCount) {
        this.month = month;
        this.activityCount = activityCount;
    }

    //getters and setters
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public int getActivityCount() {
        return activityCount;
    }
    public void setActivityCount(int activityCount) {
        this.activityCount = activityCount;
    }
    
}
