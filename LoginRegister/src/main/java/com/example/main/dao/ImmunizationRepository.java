package com.example.main.repo;

import com.example.main.entity.Immunization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ImmunizationRepository extends JpaRepository<Immunization, Long> {
    List<Immunization> findByBabyId(Long babyId);
}
