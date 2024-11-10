package com.example.main.controller;

import com.example.LoginRegister.dto.AlarmDTO;
import com.example.LoginRegister.entity.Alarm;
import com.example.LoginRegister.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/alarms")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @PostMapping("/set/{babyId}")
    public Alarm setAlarm(
            @PathVariable Long babyId,
            @RequestBody AlarmDTO alarmDTO
    ) {

        return alarmService.setAlarm(babyId, alarmDTO);
    }

    @PostMapping("/disable/{alarmId}")
    public void disableAlarm(@PathVariable Long alarmId) {
        alarmService.disableAlarm(alarmId);
    }
}

