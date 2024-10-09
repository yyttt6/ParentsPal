package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    //返回包含对话的Optional对象，如果没有找到则为空
    @Query("SELECT c FROM Conversation c " +
            "WHERE (c.user1_id = :user1_id AND c.user2_id = :user2_id) " +
            "OR (c.user1_id = :user2_id AND c.user2_id = :user1_id)")
    Optional<Conversation> findConversationByUserIds(Integer user1_id, Integer user2_id);

    @Query("SELECT c FROM Conversation c " +
            "WHERE c.user1_id = :user_id OR c.user2_id = :user_id")
    List<Conversation> findAllConversationsByUserId(Integer user_id);
}