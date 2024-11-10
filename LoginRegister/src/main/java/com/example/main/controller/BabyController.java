package com.example.main.controller;

import com.example.main.entity.Baby;
import com.example.main.service.BabyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/babies")
public class BabyController {

    @Autowired
    private BabyService babyService;

    @GetMapping
    public List<Baby> getAllBabies() {
        return babyService.getAllBabies();
    }

    @PostMapping("/add")
    public Baby addBaby(@RequestBody Baby baby) {
        return babyService.addBaby(baby);
    }
}

