package com.example.main.service.login;

import com.example.main.dto.login.AlarmDTO;
import com.example.main.dao.login.Alarm;
import com.example.main.dao.login.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlarmServiceImpl implements AlarmService{
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


    @Override
    public Alarm setAlarm(Long babyId, AlarmDTO alarmDTO) {
        Alarm alarm = new Alarm();
        alarm.setBabyId(babyId);
        alarm.setActivityType(alarmDTO.getActivityType());
        alarm.setAlarmTime(alarmDTO.getAlarmTime());
        alarm.setRecurring(alarmDTO.getIsRecurring());
        alarm.setCustomIntervalInHours(alarmDTO.getCustomIntervalInHours());
        alarm.setActive(true);
        return alarmRepository.save(alarm);
    }

    @Override
    public List<Alarm> getAlarmsByBabyId(Long babyId) {
        return alarmRepository.findByBabyId(babyId);
    }

    public List<Alarm> checkAlarms() {
        LocalDateTime now = LocalDateTime.now();
        return alarmRepository.findByIsActiveTrueAndAlarmTimeBefore(now);
    }

    @Override
    public void disableAlarm(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow();
        alarm.setActive(false);
        alarmRepository.save(alarm);
    }

}
