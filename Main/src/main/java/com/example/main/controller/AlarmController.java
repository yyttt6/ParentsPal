package com.example.main.controller;

import com.example.main.dto.AlarmDTO;
import com.example.main.entity.Alarm;
import com.example.main.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

