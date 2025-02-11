package com.example.main.controller.login;

import com.example.main.dao.login.Baby;
import com.example.main.dto.login.LoginDTO;
import com.example.main.response.LoginMessage;
import com.example.main.service.login.BabyService;
import com.example.main.service.login.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


}

