package com.example.main.service;

import com.example.main.dto.AlarmDTO;
import com.example.main.entity.Alarm;
import com.example.main.repo.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlarmService {
    @Autowired
    private AlarmRepository alarmRepository;

//    public void initializeAlarms(Long babyId) {
//        String[] types = {"feeding", "diaper", "sleep", "pumping"};
//        for (String type : types) {
//            Alarm alarm = new Alarm();
//            alarm.setBabyId(babyId);
//            alarm.setActivityType(type);
//            alarm.setAlarmTime(null);
//            alarm.setActive(true);
//            alarmRepository.save(alarm);
//        }
//    }

    public Alarm setAlarm(Long babyId, AlarmDTO alarmDTO) {
        Alarm alarm = new Alarm();
        alarm.setBabyId(babyId);
        alarm.setActivityType(alarmDTO.getActivityType());
        alarm.setAlarmTime(alarmDTO.getAlarmTime());
        alarm.setRecurring(alarmDTO.getIsRecurring());
        alarm.setFrequency(alarmDTO.getFrequency());
        alarm.setActive(true);
        return alarmRepository.save(alarm);
    }

    public List<Alarm> checkAlarms() {
        LocalDateTime now = LocalDateTime.now();
        return alarmRepository.findByIsActiveTrueAndAlarmTimeBefore(now);
    }

    public void disableAlarm(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow();
        alarm.setActive(false);
        alarmRepository.save(alarm);
    }
}

