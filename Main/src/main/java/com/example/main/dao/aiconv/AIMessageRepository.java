package com.example.main.dao.aiconv;

import com.example.main.dao.aiconv.AIMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AIMessageRepository extends JpaRepository<AIMessage, Integer> {
    List<AIMessage> findByConversationIdOrderByCreatedAtDesc(String conversationId);
}