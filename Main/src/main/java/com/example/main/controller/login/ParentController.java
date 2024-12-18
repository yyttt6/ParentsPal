package com.example.main.controller.login;

import com.example.main.dto.login.ParentDTO;
import com.example.main.dto.login.LoginDTO;
import com.example.main.dao.login.Baby;
import com.example.main.response.LoginMessage;
import com.example.main.service.login.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/{id}/upload-profile")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            parentService.uploadProfilePicture(id, file);
            return ResponseEntity.ok("Profile picture uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Long id) {
        byte[] profilePicture = parentService.getProfilePicture(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(profilePicture);
    }

    @PatchMapping("/{parentId}/change-name")
    public ResponseEntity<?> changeName(
            @PathVariable Long parentId,
            @RequestBody Map<String, String> nameData) {

        String newName = nameData.get("newName");
        LoginMessage responseMessage = parentService.changeName(parentId, newName);

        if (responseMessage.getStatus()) {
            return ResponseEntity.ok(responseMessage);
        }
        return ResponseEntity.status(400).body(responseMessage);
    }


    @PatchMapping("/{parentId}/change-phone")
    public ResponseEntity<?> changePhone(
            @PathVariable Long parentId,
            @RequestBody Map<String, String> phoneData) {

        String newPhone = phoneData.get("newPhone");
        LoginMessage responseMessage = parentService.changePhone(parentId, newPhone);

        if (responseMessage.getStatus()) {
            return ResponseEntity.ok(responseMessage);
        }
        return ResponseEntity.status(400).body(responseMessage);
    }
}
