package com.example.main.service.conv;

import com.example.main.dao.conv.*;
import com.example.main.dao.login.Parent;
import com.example.main.dao.login.ParentRepository;
import com.example.main.dto.conv.MessageDTO;
import com.example.main.converter.conv.MessageConverter;
import com.example.main.service.encry.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.main.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ParentRepository userRepository;
    @Autowired
    private MessageConverter messageConverter;
    @Autowired
    private final EncryptionService encryptionService;

    public ConversationService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }
    public Response<Long> getUserIdByUsername(String username) {

        if (username == null || username.isEmpty()) {
            return Response.newFail("Username cannot be null or empty");
        }

        Optional<Parent> userOptional = userRepository.getByName(username);
        return userOptional.map(user -> Response.newSuccess(user.getId()))
                .orElseGet(() -> Response.newFail("User not found with the given username"));
    }

    public Response<Conversation> createConversationIfNotExists(String username1, String username2) {

        Response<Long> userId1Response = getUserIdByUsername(username1);
        if (!userId1Response.isSuccess()) {
            return Response.newFail(userId1Response.getErrorMsg());
        }

        Response<Long> userId2Response = getUserIdByUsername(username2);
        if (!userId2Response.isSuccess()) {
            return Response.newFail(userId2Response.getErrorMsg());
        }

        Integer user1_id = Math.toIntExact(userId1Response.getData());
        Integer user2_id = Math.toIntExact(userId2Response.getData());
        Optional<Conversation> existingConversation = conversationRepository.findConversationByUserIds(user1_id, user2_id);

        if (existingConversation.isPresent()) {
            return Response.newSuccess(existingConversation.get());
        } else {
            Conversation conversation = new Conversation();
            conversation.setUser1Id(user1_id);
            conversation.setUser2Id(user2_id);
            conversation.setCreatedAt(LocalDateTime.now());
            Conversation savedConversation = conversationRepository.save(conversation);
            return Response.newSuccess(savedConversation);
        }
    }

    public Response<Message> saveMessage(String senderUsername, String receiverUsername, String content) {

        Response<Long> senderIdResponse = getUserIdByUsername(senderUsername);
        if (!senderIdResponse.isSuccess()) {
            return Response.newFail(senderIdResponse.getErrorMsg());
        }

        Response<Long> receiverIdResponse = getUserIdByUsername(receiverUsername);
        if (!receiverIdResponse.isSuccess()) {
            return Response.newFail(receiverIdResponse.getErrorMsg());
        }

        Integer sender_id = Math.toIntExact(senderIdResponse.getData());
        Integer receiver_id = Math.toIntExact(receiverIdResponse.getData());


        Response<Conversation> conversationResponse = createConversationIfNotExists(senderUsername, receiverUsername);
        if (!conversationResponse.isSuccess()) {
            return Response.newFail(conversationResponse.getErrorMsg());
        }
        Integer conversation_id = conversationResponse.getData().getConversationId();


        Message message = new Message();
        message.setConversationId(conversation_id);
        message.setSenderId(sender_id);
        message.setReceiverId(receiver_id);
        message.setContent(encryptionService.encrypt(content));
        message.setCreatedAt(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);
        return Response.newSuccess(savedMessage);
    }
    public Response<List<MessageDTO>> getLatestMessagesForUser(String username) {
        Response<Long> userIdResponse = getUserIdByUsername(username);
        if (!userIdResponse.isSuccess()) {
            return Response.newFail(userIdResponse.getErrorMsg());
        }

        Integer user_id = Math.toIntExact(userIdResponse.getData());

        List<Conversation> conversations = conversationRepository.findAllConversationsByUserId(user_id);
        List<MessageDTO> latestMessages = conversations.stream()
                .map(conversation -> messageRepository.findLatestMessageByConversationId(conversation.getConversationId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(messageConverter::convertMessage)
                .collect(Collectors.toList());

        return Response.newSuccess(latestMessages);
    }
    public Response<List<MessageDTO>> getLast100MessagesBetweenUsers(String username1, String username2) {
        Response<Long> userId1Response = getUserIdByUsername(username1);
        if (!userId1Response.isSuccess()) {
            return Response.newFail(userId1Response.getErrorMsg());
        }

        Response<Long> userId2Response = getUserIdByUsername(username2);
        if (!userId2Response.isSuccess()) {
            return Response.newFail(userId2Response.getErrorMsg());
        }

        Integer user1_id = Math.toIntExact(userId1Response.getData());
        Integer user2_id = Math.toIntExact(userId2Response.getData());

        Optional<Conversation> conversationOptional = conversationRepository.findConversationByUserIds(user1_id, user2_id);
        if (conversationOptional.isPresent()) {
            Conversation conversation = conversationOptional.get();
            List<Message> messages = messageRepository.findTop100MessagesByConversationId(conversation.getConversationId());
            List<MessageDTO> messageDTOs = messages.stream()
                    .map(messageConverter::convertMessage)
                    .collect(Collectors.toList());
            return Response.newSuccess(messageDTOs);
        } else {
            return Response.newFail("No conversation found between the users.");
        }
    }

    public void setConversationRepository(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void setUserRepository(ParentRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }
}