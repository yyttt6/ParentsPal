package com.example.main.service;
import com.example.main.entity.Baby;
import com.example.main.repo.BabyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BabyService {

    private final BabyRepository babyRepository;

    @Autowired
    public BabyService(BabyRepository babyRepository) {
        this.babyRepository = babyRepository;
    }

    // Create or update a baby
    public Baby addBaby(Baby baby) {
        return babyRepository.save(baby);
    }

    // Get a baby by ID
    public Optional<Baby> getBabyById(Long id) {
        return babyRepository.findById(id);
    }

    // Get all babies
    public List<Baby> getAllBabies() {
        return babyRepository.findAll();
    }

    // Delete a baby by ID
    public void deleteBaby(Long id) {
        babyRepository.deleteById(id);
    }
}

