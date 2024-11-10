package com.example.demo.converter.conv;

import com.example.demo.dao.conv.Message;
import com.example.demo.dao.conv.UserRepository;
import com.example.demo.dto.conv.MessageDTO;
import com.example.demo.service.conv.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageConverter {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final EncryptionService encryptionService;

    public MessageConverter(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public MessageDTO convertMessage(Message message){
        MessageDTO messageDTO = new MessageDTO();

        Optional<String> nameOptional = userRepository.findNameByUserId(message.getSenderId());
        String senderName = nameOptional.orElse("Unknown");
        messageDTO.setSender_name(senderName);
        nameOptional = userRepository.findNameByUserId(message.getReceiverId());
        String receiverName = nameOptional.orElse("Unknown");
        messageDTO.setReceiver_name(receiverName);

        messageDTO.setContent(encryptionService.decrypt(message.getContent()));
        messageDTO.setCreated_at(message.getCreatedAt());
        return messageDTO;
    }
}
