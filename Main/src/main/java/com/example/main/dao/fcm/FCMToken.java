package com.example.main.dao.fcm;
import jakarta.persistence.*;

@Entity
@Table(name = "FCMToken", indexes = {@Index(columnList = "user_id", unique = true)})
public class FCMToken {
    @Id
    private Long userId; // 修改为 Long

    @Column(length = 255, nullable = false, columnDefinition = "VARCHAR(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String token;

    public FCMToken() {}

    public FCMToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}