package com.example.main.service.login;
import com.example.main.dao.login.Baby;
import com.example.main.dao.login.GrowthTracking;
import com.example.main.dao.login.BabyRepository;
import com.example.main.dao.login.GrowthTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GrowthTrackingService {
    @Autowired
    private GrowthTrackingRepository growthTrackingRepository;
    @Autowired
    private BabyRepository babyRepository;

    public GrowthTracking addGrowthTracking(Long babyId, Double weight, Double height,  LocalDate measurementDate) {
        Baby existingBaby = babyRepository.findById(babyId).orElse(null);

        GrowthTracking growthTracking = new GrowthTracking(weight, height, measurementDate, existingBaby);

        return growthTrackingRepository.save(growthTracking);
    }

    public List<GrowthTracking> getGrowthTrackingByBabyId(Long babyId) {
        return growthTrackingRepository.findByBabyId(babyId);
    }
}
