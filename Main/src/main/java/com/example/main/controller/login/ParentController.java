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
@RequestMapping("api/appuser")
public class ParentController {
    @Autowired
    private ParentService parentService;
    @PostMapping(path = "/register")
    public ResponseEntity<?> saveAppUser(@RequestBody ParentDTO parentDTO)
    {
        LoginMessage responseMessage = parentService.addNewAppUser(parentDTO);
        if (responseMessage.getStatus()) {
            return ResponseEntity.ok(responseMessage );
        }
        return ResponseEntity.status(409).body(responseMessage );
    }
    @PostMapping(path = "/login")
    public ResponseEntity<?> loginAppUser(@RequestBody LoginDTO loginDTO)
    {
        LoginMessage loginMessage =  parentService.loginAppUser(loginDTO);
        if (loginMessage.getStatus()) {
            Long parentId = parentService.getParentIdByPhoneNumber(loginDTO.getPhoneNumber());
            loginMessage.setParentId(parentId);
            String parentName = parentService.getParentNameById(parentId);
            loginMessage.setParentName(parentName);


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

    @PatchMapping("/{parentId}/change-password")
    public ResponseEntity<?> changePassword(
        @PathVariable Long parentId,
        @RequestBody Map<String, String> passwordData) {

        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");
    
        LoginMessage responseMessage = parentService.changePassword(parentId, oldPassword, newPassword);
        if (responseMessage.getStatus()) {
            return ResponseEntity.ok(responseMessage);
        }
        return ResponseEntity.status(400).body(responseMessage);
    }
}
