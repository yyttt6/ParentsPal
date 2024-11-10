package com.example.main.controller;

import com.example.LoginRegister.dto.ParentDTO;
import com.example.LoginRegister.dto.LoginDTO;
import com.example.LoginRegister.entity.Baby;
import com.example.LoginRegister.entity.Parent;
import com.example.LoginRegister.response.LoginMessage;
import com.example.LoginRegister.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/appuser")
public class ParentController {
    @Autowired
    private ParentService parentService;
    @PostMapping(path = "/register")
    public String saveAppUser(@RequestBody ParentDTO parentDTO)
    {
        String name =  parentService.addNewAppUser(parentDTO);
        return name;
    }
    @PostMapping(path = "/login")
    public ResponseEntity<?> loginAppUser(@RequestBody LoginDTO loginDTO)
    {
        LoginMessage loginMessage =  parentService.loginAppUser(loginDTO);
        return ResponseEntity.ok(loginMessage);
    }

    @PostMapping("/{parentId}/babies")
    public ResponseEntity<Baby> addBabyToParent(@PathVariable Long parentId, @RequestBody Baby baby) {
        Baby addedBaby = parentService.addBabyToParent(parentId, baby);
        return ResponseEntity.ok(addedBaby);
    }
}
