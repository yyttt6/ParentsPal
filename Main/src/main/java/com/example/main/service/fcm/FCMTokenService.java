package com.example.main.service.fcm;

import com.example.main.Response;
import com.example.main.dao.fcm.FCMToken;
import com.example.main.dao.fcm.FCMTokenRepository;
import com.example.main.dao.login.Parent;
import com.example.main.dao.login.ParentRepository;
import com.example.main.service.encry.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FCMTokenService {

    private final FCMTokenRepository fcmTokenRepository;
    private final EncryptionService encryptionService;
    @Autowired
    private ParentRepository userRepository;

    @Autowired
    public FCMTokenService(FCMTokenRepository fcmTokenRepository, EncryptionService encryptionService) {
        this.fcmTokenRepository = fcmTokenRepository;
        this.encryptionService = encryptionService;
    }
    public Response<Long> getUserIdByUsername(String username) {

        if (username == null || username.isEmpty()) {
            return Response.newFail("Username cannot be null or empty");
        }
        Optional<Parent> userOptional = userRepository.getByName(username);
        return userOptional.map(user -> Response.newSuccess(user.getId()))
                .orElseGet(() -> Response.newFail("User not found with the given username"));
    }

    public String getTokenByUserId(Long userId) {
        return fcmTokenRepository.findByUserId(userId)
                .map(fcmToken -> encryptionService.decrypt(fcmToken.getToken()))
                .orElseThrow(() -> new RuntimeException("User ID not found"));
    }

    public Response<String> upsertToken(String userName, String token) {

        Response<Long> userIdResponse = getUserIdByUsername(userName);
        if (!userIdResponse.isSuccess()) {
            return Response.newFail(userIdResponse.getErrorMsg());
        }
        Long userId = userIdResponse.getData();

        FCMToken existingToken = fcmTokenRepository.findByUserId(userId)
                .orElse(null);

        if (existingToken == null) {
            existingToken = new FCMToken();
            existingToken.setUserId(userId);
        }
        existingToken.setToken(encryptionService.encrypt(token));
        fcmTokenRepository.save(existingToken);
        return Response.newSuccess("ok");
    }
}