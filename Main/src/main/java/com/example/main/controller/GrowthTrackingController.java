package com.example.main.controller;

import com.example.LoginRegister.dto.GrowthTrackingDTO;
import com.example.LoginRegister.entity.GrowthTracking;
import com.example.LoginRegister.service.GrowthTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/babies")
public class GrowthTrackingController {
    @Autowired
    private GrowthTrackingService growthTrackingService;

    @PostMapping("/{babyId}/growth")
    public ResponseEntity<GrowthTracking> addGrowthTracking(
            @PathVariable Long babyId,
            @RequestBody GrowthTrackingDTO growthTrackingDTO) {

        GrowthTracking growthTracking = growthTrackingService.addGrowthTracking(
                babyId,
                growthTrackingDTO.getWeight(),
                growthTrackingDTO.getHeight(),
                growthTrackingDTO.getMeasurementDate());
        return ResponseEntity.ok(growthTracking);
    }

    @GetMapping("/{babyId}/growth")
    public ResponseEntity<List<GrowthTracking>> getGrowthTracking(@PathVariable Long babyId) {
        List<GrowthTracking> growthTrackings = growthTrackingService.getGrowthTrackingByBabyId(babyId);
        return ResponseEntity.ok(growthTrackings);
    }
}

