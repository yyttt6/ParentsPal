package com.example.main.controller.login;

import com.example.main.dto.login.ParentDTO;
import com.example.main.dto.login.LoginDTO;
import com.example.main.dao.login.Baby;
import com.example.main.response.LoginMessage;
import com.example.main.service.login.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/appuser")
public class ParentController {
    @Autowired
    private ParentService parentService;
    @PostMapping(path = "/register")
    public ResponseEntity<?> saveAppUser(@RequestBody ParentDTO parentDTO)
    {
        String responseMessage = parentService.addNewAppUser(parentDTO);
        if (responseMessage.equals("Phone number already exists")) {
            return ResponseEntity.status(409).body("Phone number already exists");
        }
        return ResponseEntity.ok("Registration successful");
    }
    @PostMapping(path = "/login")
    public ResponseEntity<?> loginAppUser(@RequestBody LoginDTO loginDTO)
    {
        LoginMessage loginMessage =  parentService.loginAppUser(loginDTO);
        if (loginMessage.getStatus()) {
            Long parentId = parentService.getParentIdByPhoneNumber(loginDTO.getPhoneNumber());
            loginMessage.setParentId(parentId);

            List<Baby> babies = parentService.getBabiesByParentId(parentId);
            loginMessage.setBabies(babies);
            return ResponseEntity.ok(loginMessage);
        } else {
            return ResponseEntity.status(401).body(loginMessage);
        }
    }

    @PostMapping("/{parentId}/babies")
    public ResponseEntity<Baby> addBabyToParent(@PathVariable Long parentId, @RequestBody Baby baby) {
        Baby addedBaby = parentService.addBabyToParent(parentId, baby);
        return ResponseEntity.ok(addedBaby);
    }
}
