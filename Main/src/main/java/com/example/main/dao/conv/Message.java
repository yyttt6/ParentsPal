package com.example.main.dao.conv;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Message",
        indexes = @Index(columnList = "conversation_id, timestamp DESC", name = "idx_message_conversation_timestamp"))
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer message_id;

    @Column(nullable = false)
    private Integer conversation_id;

    @Column(nullable = false)
    private Integer sender_id;

    @Column(nullable = false)
    private Integer receiver_id;

    @Column(length = 1000, nullable = false, columnDefinition = "VARCHAR(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String content;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at;

    public Integer getMessageId() {
        return message_id;
    }

    public void setMessageId(Integer message_id) {
        this.message_id = message_id;
    }

    public Integer getConversationId() {
        return conversation_id;
    }

    public void setConversationId(Integer conversation_id) {
        this.conversation_id = conversation_id;
    }

    public Integer getSenderId() {
        return sender_id;
    }

    public void setSenderId(Integer sender_id) {
        this.sender_id = sender_id;
    }

    public Integer getReceiverId() {
        return receiver_id;
    }

    public void setReceiverId(Integer receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}