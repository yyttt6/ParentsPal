package com.example.main.converter.aiconv;

import com.example.main.dao.aiconv.AIMessage;
import com.example.main.dto.aiconv.AIMessageDTO;
import org.springframework.stereotype.Component;

@Component
public class AIMessageConverter {

    public AIMessageDTO convertAIMessage(AIMessage mess){
        AIMessageDTO aiMessageDTO = new AIMessageDTO();
        aiMessageDTO.setConv_id(mess.getConversationId());
        aiMessageDTO.setQuery(mess.getQuery());
        aiMessageDTO.setAnswer(mess.getAnswer());
        aiMessageDTO.setCreated_at(mess.getCreatedAt());
        return aiMessageDTO;
    }
}
