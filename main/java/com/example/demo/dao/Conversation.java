package com.example.demo.dao;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "conversation",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"}),
        indexes = @Index(columnList = "user1_id, user2_id", name = "idx_conversation_users"))
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer conversation_id;

    @Column(nullable = false)
    private Integer user1_id;

    @Column(nullable = false)
    private Integer user2_id;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at;

    // Getters and Setters
    public Integer getConversationId() {
        return conversation_id;
    }

    public void setConversationId(Integer conversation_id) {
        this.conversation_id = conversation_id;
    }

    public Integer getUser1Id() {
        return user1_id;
    }

    public void setUser1Id(Integer user1_id) {
        this.user1_id = user1_id;
    }

    public Integer getUser2Id() {
        return user2_id;
    }

    public void setUser2Id(Integer user2_id) {
        this.user2_id = user2_id;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}