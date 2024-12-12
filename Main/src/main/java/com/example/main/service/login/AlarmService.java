package com.example.main.service.login;

import com.example.main.dao.login.Alarm;
import com.example.main.dto.login.AlarmDTO;

import java.util.List;

public interface AlarmService {
    Alarm setAlarm(Long babyId, AlarmDTO alarmDTO);
    void disableAlarm(Long alarmId);
    List<Alarm> getAlarmsByBabyId(Long babyId);
    void deleteAlarm(Long alarmId);
}


