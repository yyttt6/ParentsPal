package com.example.demo.controller;

import com.example.demo.Response;
import com.example.demo.dao.Conversation;
import com.example.demo.dao.Message;
import com.example.demo.dto.MessageDTO;
import com.example.demo.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @PostMapping("/create")
    public Response<Conversation> createConversation(@RequestParam String username1, @RequestParam String username2) {
        return conversationService.createConversationIfNotExists(username1, username2);
    }

    @PostMapping("/message")
    public Response<Message> saveMessage(@RequestParam String senderUsername, @RequestParam String receiverUsername,
                                         @RequestParam String content, @RequestParam(required = false) Integer conversation_id) {
        return conversationService.saveMessage(senderUsername, receiverUsername, content, conversation_id);
    }

    @GetMapping("/latest-messages")
    public Response<List<MessageDTO>> getLatestMessagesForUser(@RequestParam String username) {
        return conversationService.getLatestMessagesForUser(username);
    }

    @GetMapping("/messages-between-users")
    public Response<List<MessageDTO>> getLast100MessagesBetweenUsers(@RequestParam String username1, @RequestParam String username2) {
        return conversationService.getLast100MessagesBetweenUsers(username1, username2);
    }
}