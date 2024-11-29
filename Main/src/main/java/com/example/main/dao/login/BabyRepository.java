package com.example.main.dao.login;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BabyRepository extends JpaRepository<Baby, Long> {
    List<Baby> findByParentId(Long parentId);
}

