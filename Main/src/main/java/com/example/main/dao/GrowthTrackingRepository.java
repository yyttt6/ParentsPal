package com.example.main.repo;

import com.example.main.entity.GrowthTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrowthTrackingRepository extends JpaRepository<GrowthTracking, Long> {
    List<GrowthTracking> findByBabyId(Long babyId);
}