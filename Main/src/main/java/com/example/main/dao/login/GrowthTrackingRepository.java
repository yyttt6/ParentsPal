package com.example.main.dao.login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrowthTrackingRepository extends JpaRepository<GrowthTracking, Long> {
    List<GrowthTracking> findByBabyId(Long babyId);
    Optional<GrowthTracking> findByIdAndBabyId(Long id, Long babyId);
}
