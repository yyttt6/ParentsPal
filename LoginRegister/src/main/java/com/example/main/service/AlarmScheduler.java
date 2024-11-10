package com.example.main.service;
import com.example.main.dto.AlarmDTO;
import com.example.main.entity.Alarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AlarmScheduler {

    @Autowired
    private AlarmService alarmService;

    @Scheduled(fixedRate = 60000)
    public void checkAndTriggerAlarms() {
        List<Alarm> dueAlarms = alarmService.checkAlarms();

        for (Alarm alarm : dueAlarms) {
            triggerAlarm(alarm);


            if (!alarm.getRecurring()) {
                alarmService.disableAlarm(alarm.getId());
            } else {

                LocalDateTime nextAlarmTime = calculateNextAlarmTime(alarm);

                AlarmDTO alarmDTO = new AlarmDTO();
                alarmDTO.setActivityType(alarm.getActivityType());
                alarmDTO.setAlarmTime(nextAlarmTime);
                alarmDTO.setIsRecurring(alarm.getRecurring());
                alarmDTO.setFrequency(alarm.getFrequency());

                alarmService.setAlarm(alarm.getBabyId(), alarmDTO);
            }
        }
    }

    private void triggerAlarm(Alarm alarm) {
        System.out.println("Triggering alarm for activity: " + alarm.getActivityType());
    }

    private LocalDateTime calculateNextAlarmTime(Alarm alarm) {
        switch (alarm.getFrequency()) {
            case "hourly": return alarm.getAlarmTime().plusHours(1);
            case "daily": return alarm.getAlarmTime().plusDays(1);
            default: return alarm.getAlarmTime();
        }
    }
}

