package com.example.main.service.login;
import com.example.main.dto.login.AlarmDTO;
import com.example.main.dao.login.Alarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AlarmScheduler {

    @Autowired
    private AlarmServiceImpl alarmServiceImpl;

    @Scheduled(fixedRate = 60000)
    public void checkAndTriggerAlarms() {
        List<Alarm> dueAlarms = alarmServiceImpl.checkAlarms();

        for (Alarm alarm : dueAlarms) {
            triggerAlarm(alarm);


            if (!alarm.getRecurring()) {
                alarmServiceImpl.disableAlarm(alarm.getId());
            } else {

                LocalDateTime nextAlarmTime = calculateNextAlarmTime(alarm);

                AlarmDTO alarmDTO = new AlarmDTO();
                alarmDTO.setActivityType(alarm.getActivityType());
                alarmDTO.setAlarmTime(nextAlarmTime);
                alarmDTO.setIsRecurring(alarm.getRecurring());
                alarmDTO.setCustomIntervalInHours(alarm.getCustomIntervalInHours());

                alarmServiceImpl.setAlarm(alarm.getBabyId(), alarmDTO);
            }
        }
    }

    private void triggerAlarm(Alarm alarm) {
        System.out.println("Triggering alarm for activity: " + alarm.getActivityType());
    }

    private LocalDateTime calculateNextAlarmTime(Alarm alarm) {
        return alarm.getAlarmTime().plusHours(alarm.getCustomIntervalInHours());
    }
}

