package com.example.main.repo;

import com.example.main.entity.Baby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BabyRepository extends JpaRepository<Baby, Long> {}

