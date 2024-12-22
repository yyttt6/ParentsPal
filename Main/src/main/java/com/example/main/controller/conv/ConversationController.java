package com.example.main.controller.conv;

import com.example.main.Response;
import com.example.main.dao.conv.Conversation;
import com.example.main.dao.conv.Message;
import com.example.main.dto.conv.MessageDTO;
import com.example.main.dto.conv.CreateConversationDTO;
import com.example.main.dto.conv.SaveMessageDTO;
import com.example.main.service.conv.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @PostMapping("/create")
    public Response<Conversation> createConversation(@RequestBody CreateConversationDTO dto) {
        return conversationService.createConversationIfNotExists(dto.getUsername1(), dto.getUsername2());
    }

    @PostMapping("/message")
    public Response<Message> saveMessage(@RequestBody SaveMessageDTO dto) {
        return conversationService.saveMessage(dto.getSenderUsername(), dto.getReceiverUsername(), dto.getContent());
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