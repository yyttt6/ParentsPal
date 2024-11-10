package com.example.LoginRegister.repo;

import com.example.LoginRegister.entity.Baby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BabyRepository extends JpaRepository<Baby, Long> {}

