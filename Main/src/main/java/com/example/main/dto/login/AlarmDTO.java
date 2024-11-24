package com.example.main.dto.login;

import java.time.LocalDateTime;

public class AlarmDTO {
    private String activityType;
    private LocalDateTime alarmTime;
    private Boolean isRecurring;
    private Integer customIntervalInHours;

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public LocalDateTime getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(LocalDateTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public Integer getCustomIntervalInHours() {
        return customIntervalInHours;
    }

    public void setCustomIntervalInHours(Integer customIntervalInHours) {
        this.customIntervalInHours = customIntervalInHours;
    }
}


