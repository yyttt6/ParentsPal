package com.example.main.firebase.conv;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

@Service
public class FcmNotificationService {
//    private final FirebaseMessaging firebaseMessaging;
//
//    public FcmNotificationService(FirebaseApp firebaseApp) {
//
//        this.firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
//    }
//
//
//    public String sendNotification(String token, String title, String body) {
//        Message message = Message.builder()
//                .putData("title", title)
//                .putData("body", body)
//                .setToken(token)
//                .build();
//
//        try {
//            String response =  this.firebaseMessaging.send(message);
//            return "Successfully sent message: " + response;
//        } catch (FirebaseMessagingException e) {
//            return "Error sending message: " + e.getMessage();
//        }
//    }
//
//    public String sendChatMessageNotification(String token, String fromUser, String message) {
//        Notification notification = Notification.builder()
//                .setTitle(fromUser + " sent a message")
//                .setBody(message)
//                .build();
//        Message fcmMessage = Message.builder()
//                .setNotification(notification)
//                .setToken(token)
//                .build();
//        try {
//            String response =  this.firebaseMessaging.send(fcmMessage);
//            return "Successfully sent message: " + response;
//        } catch (FirebaseMessagingException e) {
//            return "Error sending message: " + e.getMessage();
//        }
//    }
}