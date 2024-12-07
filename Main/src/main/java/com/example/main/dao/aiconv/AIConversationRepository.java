package com.example.main.dao.aiconv;

import com.example.main.dao.aiconv.AIConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AIConversationRepository extends JpaRepository<AIConversation, String> {
    List<AIConversation> findByUserId(Long userId);
}