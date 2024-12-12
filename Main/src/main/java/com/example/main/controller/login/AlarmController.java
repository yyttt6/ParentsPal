package com.example.main.controller.login;

import com.example.main.dto.login.AlarmDTO;
import com.example.main.dao.login.Alarm;
import com.example.main.service.login.AlarmServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alarms")
public class AlarmController {

    @Autowired
    private AlarmServiceImpl alarmService;

    @PostMapping("/set/{babyId}")
    public Alarm setAlarm(
            @PathVariable Long babyId,
            @RequestBody AlarmDTO alarmDTO
    ) {

        return alarmService.setAlarm(babyId, alarmDTO);
    }

    @GetMapping("/get/{babyId}")
    public List<Alarm> getAlarmsByBabyId(@PathVariable Long babyId) {
        return alarmService.getAlarmsByBabyId(babyId);
    }

    @PostMapping("/disable/{alarmId}")
    public void disableAlarm(@PathVariable Long alarmId) {
        alarmService.disableAlarm(alarmId);
    }

    @DeleteMapping("/delete/{alarmId}")
    public ResponseEntity<String> deleteAlarm(@PathVariable Long alarmId) {
        alarmService.deleteAlarm(alarmId);
        return ResponseEntity.ok("Alarm deleted successfully");
    }
}

