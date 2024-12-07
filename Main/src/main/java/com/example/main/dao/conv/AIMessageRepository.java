package com.example.main.dao.conv;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AIMessageRepository extends JpaRepository<AIMessage, Integer> {
    List<AIMessage> findByConversationIdOrderByCreatedAtDesc(String conversationId);
}