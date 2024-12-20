package com.example.main.converter.conv;

import com.example.main.dao.conv.Message;
import com.example.main.dao.login.Parent;
import com.example.main.dao.login.ParentRepository;
import com.example.main.dto.conv.MessageDTO;
import com.example.main.service.encry.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageConverter {
    @Autowired
    private ParentRepository userRepository;
    @Autowired
    private final EncryptionService encryptionService;

    public MessageConverter(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public MessageDTO convertMessage(Message message){
        MessageDTO messageDTO = new MessageDTO();

        Optional<Parent> optionalParent1 = userRepository.findById(Long.valueOf(message.getSenderId()));
        String senderName = optionalParent1.map(Parent::getName).orElse("Unknown");
        messageDTO.setSender_name(senderName);
        messageDTO.setSender_id(Long.valueOf(message.getSenderId()));
        Optional<Parent> optionalParent2 = userRepository.findById(Long.valueOf(message.getReceiverId()));
        String receiverName = optionalParent2.map(Parent::getName).orElse("Unknown");
        messageDTO.setReceiver_id(Long.valueOf(message.getReceiverId()));
        messageDTO.setReceiver_name(receiverName);

        messageDTO.setContent(encryptionService.decrypt(message.getContent()));
        messageDTO.setCreated_at(message.getCreatedAt());
        return messageDTO;
    }
}
