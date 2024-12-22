package com.example.main.converter.aiconv;

import com.example.main.dao.aiconv.AIConversation;
import com.example.main.dto.aiconv.ConversationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.main.service.encry.EncryptionService;


@Component
public class ConversationConverter {
    @Autowired
    private final EncryptionService encryptionService;
    public ConversationConverter(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public ConversationDTO convertConversation(AIConversation conv){
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setConv_id(conv.getConversationId());
        conversationDTO.setConv_name(encryptionService.decrypt(conv.getConversationName()));
        conversationDTO.setCreated_at(conv.getCreatedAt());
        return conversationDTO;
    }
}
