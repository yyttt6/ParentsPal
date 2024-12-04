package com.example.main.controller.fcm;

import com.example.main.Response;
import com.example.main.dto.fcm.OnTokenDTO;
import com.example.main.service.fcm.FCMTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fcm")
public class FCMTokenController {

    private final FCMTokenService fcmTokenService;

    @Autowired
    public FCMTokenController(FCMTokenService fcmTokenService) {
        this.fcmTokenService = fcmTokenService;
    }

    @PostMapping("/token")
    public Response<String> upsertToken(@RequestBody OnTokenDTO dto) {
        try {
            return fcmTokenService.upsertToken(dto.getUserName(), dto.getToken());
        } catch (Exception e) {
            return Response.newFail("Failed to update or insert token: " + e.getMessage());
        }
    }

}