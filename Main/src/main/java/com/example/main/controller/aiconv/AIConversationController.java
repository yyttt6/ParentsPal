package com.example.main.controller.aiconv;

import com.example.main.Response;
import com.example.main.dao.aiconv.AIMessage;
import com.example.main.dto.aiconv.AIMessageDTO;
import com.example.main.dto.aiconv.ConversationDTO;
import com.example.main.dto.aiconv.newMessageDTO;
import com.example.main.service.aiconv.AIConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai_conversations")
public class AIConversationController {
    @Autowired
    private AIConversationService aiConversationService;

    @PostMapping("/message")
    public Response<AIMessage> newAIMessage(@RequestBody newMessageDTO dto) {
        return aiConversationService.getAIMessage(dto.getUsername(), dto.getQuery(), dto.getMode(), dto.getConversationId());
    }

    @GetMapping("/get-conv")
    public Response<List<ConversationDTO>> getAIConversations(@RequestParam String username) {
        return aiConversationService.getAllConversation(username);
    }

    @GetMapping("/get-message")
    public Response<List<AIMessageDTO>> getAIMessages(@RequestParam String convid) {
        return aiConversationService.getAllMessage(convid);
    }
}
