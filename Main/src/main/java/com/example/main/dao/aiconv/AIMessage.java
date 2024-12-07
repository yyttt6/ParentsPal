package com.example.main.dao.aiconv;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "aimessage",
        indexes = {
                @Index(name = "idx_message_conversation_timestamp", columnList = "conversation_id, created_at")
        })
public class AIMessage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    @Column(name = "conversation_id", length = 255, nullable = false)
    private String conversationId;
    @Column(name = "query", length = 2000, nullable = false)
    private String query;
    @Column(name = "answer", length = 4000, nullable = false)
    private String answer;
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}