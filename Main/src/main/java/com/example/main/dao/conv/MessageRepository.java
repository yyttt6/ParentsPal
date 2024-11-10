package com.example.main.dao.conv;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    <S extends Message> @Nonnull S save(@Nonnull S message);
    @Query("SELECT m FROM Message m " +
            "WHERE m.conversation_id = :conversation_id " +
            "ORDER BY m.created_at DESC LIMIT 1")
    Optional<Message> findLatestMessageByConversationId(Integer conversation_id);

    @Query(value = "SELECT * FROM Message m " +
            "WHERE m.conversation_id = :conversation_id " +
            "ORDER BY m.created_at DESC LIMIT 100", nativeQuery = true)
    List<Message> findTop100MessagesByConversationId(Integer conversation_id);
}