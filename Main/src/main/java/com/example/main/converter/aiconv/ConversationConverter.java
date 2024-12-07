package com.example.main.converter.aiconv;

import com.example.main.dao.aiconv.AIConversation;
import com.example.main.dto.aiconv.ConversationDTO;
import org.springframework.stereotype.Component;

@Component
public class ConversationConverter {

    public ConversationDTO convertConversation(AIConversation conv){
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setConv_id(conv.getConversationId());
        conversationDTO.setConv_name(conv.getConversationName());
        conversationDTO.setCreated_at(conv.getCreatedAt());
        return conversationDTO;
    }
}
