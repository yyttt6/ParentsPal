package com.example.main.dao.login;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByIsActiveTrueAndAlarmTimeBefore(LocalDateTime now);

    List<Alarm> findByBabyId(Long babyId);
}
