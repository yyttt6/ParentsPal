package com.example.main.converter.aiconv;

import com.example.main.dao.aiconv.AIMessage;
import com.example.main.dto.aiconv.AIMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.main.service.encry.EncryptionService;

@Component
public class AIMessageConverter {
    @Autowired
    private final EncryptionService encryptionService;

    public AIMessageConverter(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public AIMessageDTO convertAIMessage(AIMessage mess){
        AIMessageDTO aiMessageDTO = new AIMessageDTO();
        aiMessageDTO.setConv_id(mess.getConversationId());
        aiMessageDTO.setQuery(encryptionService.decrypt(mess.getQuery()));
        aiMessageDTO.setAnswer(encryptionService.decrypt(mess.getAnswer()));
        aiMessageDTO.setCreated_at(mess.getCreatedAt());
        return aiMessageDTO;
    }
}
