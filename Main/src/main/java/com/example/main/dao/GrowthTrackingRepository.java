package com.example.LoginRegister.repo;

import com.example.LoginRegister.entity.GrowthTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrowthTrackingRepository extends JpaRepository<GrowthTracking, Long> {
    List<GrowthTracking> findByBabyId(Long babyId);
}