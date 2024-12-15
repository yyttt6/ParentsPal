package com.example.main;
import com.example.main.dao.conv.ConversationRepository;
import com.example.main.dao.conv.MessageRepository;
import com.example.main.dao.login.ParentRepository;
import com.example.main.dto.conv.MessageDTO;
import com.example.main.firebase.FCMService;
import com.example.main.service.encry.EncryptionService;
import com.example.main.service.fcm.FCMTokenService;
import com.example.main.converter.conv.MessageConverter;
import com.example.main.dao.conv.Message;
import com.example.main.dao.conv.Conversation;
import com.example.main.dao.login.Parent;
import com.example.main.service.conv.ConversationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;

class ConversationServiceTest {

    @Mock
    private ConversationRepository conversationRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ParentRepository userRepository;
    @Mock
    private EncryptionService encryptionService;
    @Mock
    private FCMTokenService fcmTokenService;
    @Mock
    private FCMService fcmNotificationService;
    @Mock
    private MessageConverter messageConverter;
    private ConversationService conversationService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        conversationService = new ConversationService(encryptionService, fcmNotificationService, fcmTokenService);
        conversationService.setUserRepository(userRepository);
        conversationService.setConversationRepository(conversationRepository);
        conversationService.setMessageRepository(messageRepository);
        conversationService.setMessageConverter(messageConverter);
    }
    @Test
    void testGetUserIdByUsername_UserNotFound() {
        String username = "testuser";
        when(userRepository.getByName(username)).thenReturn(Optional.empty());
        Response<Long> response = conversationService.getUserIdByUsername(username);
        assertFalse(response.isSuccess());
        assertEquals("User not found with the given username", response.getErrorMsg());
    }

    @Test
    void testGetUserIdByUsername_Success() {
        String username = "testuser";
        Parent parent = new Parent();
        parent.setId(1L);
        when(userRepository.getByName(username)).thenReturn(Optional.of(parent));
        Response<Long> response = conversationService.getUserIdByUsername(username);
        assertTrue(response.isSuccess());
        assertEquals(1L, response.getData());
    }

    @Test
    void testCreateConversationIfNotExists_ConversationExists() {
        String username1 = "user1";
        String username2 = "user2";
        Long user1_id = 1L;
        Long user2_id = 2L;
        Parent parent1 = new Parent();
        parent1.setId(user1_id);
        Parent parent2 = new Parent();
        parent2.setId(user2_id);
        when(userRepository.getByName(username1)).thenReturn(Optional.of(parent1));
        when(userRepository.getByName(username2)).thenReturn(Optional.of(parent2));
        Conversation existingConversation = new Conversation();
        existingConversation.setConversationId(123);
        when(conversationRepository.findConversationByUserIds(Math.toIntExact(user1_id), Math.toIntExact(user2_id))).thenReturn(Optional.of(existingConversation));
        Response<Conversation> response = conversationService.createConversationIfNotExists(username1, username2);
        assertTrue(response.isSuccess());
        assertEquals(123, response.getData().getConversationId());
    }

    @Test
    void testCreateConversationIfNotExists_ConversationCreated() {
        String username1 = "user1";
        String username2 = "user2";
        Long user1_id = 1L;
        Long user2_id = 2L;
        Parent parent1 = new Parent();
        parent1.setId(user1_id);
        Parent parent2 = new Parent();
        parent2.setId(user2_id);
        when(userRepository.getByName(username1)).thenReturn(Optional.of(parent1));
        when(userRepository.getByName(username2)).thenReturn(Optional.of(parent2));
        when(conversationRepository.findConversationByUserIds(Math.toIntExact(user1_id), Math.toIntExact(user2_id))).thenReturn(Optional.empty());
        Conversation savedConversation = new Conversation();
        savedConversation.setConversationId(123);
        when(conversationRepository.save(any(Conversation.class))).thenReturn(savedConversation);
        Response<Conversation> response = conversationService.createConversationIfNotExists(username1, username2);
        assertTrue(response.isSuccess());
        assertEquals(123, response.getData().getConversationId());
    }

    @Test
    void testSaveMessage_Success() {
        String username1 = "user1";
        String username2 = "user2";
        Long user1_id = 1L;
        Long user2_id = 2L;
        Parent parent1 = new Parent();
        parent1.setId(user1_id);
        Parent parent2 = new Parent();
        parent2.setId(user2_id);
        when(userRepository.getByName(username1)).thenReturn(Optional.of(parent1));
        when(userRepository.getByName(username2)).thenReturn(Optional.of(parent2));
        String content = "Hello, World!";
        Conversation conversation = new Conversation();
        conversation.setConversationId(1);
        when(conversationRepository.findConversationByUserIds(1, 2)).thenReturn(Optional.of(conversation));
        Message savedMessage = new Message();
        savedMessage.setMessageId(1);
        savedMessage.setContent(content);
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);
        doNothing().when(fcmNotificationService).sendMessageToDevice(anyString(), anyString(), anyString());
        Response<Message> response = conversationService.saveMessage(username1, username2, content);
        assertTrue(response.isSuccess());
        assertEquals("Hello, World!", response.getData().getContent());
    }

    @Test
    void testGetLatestMessagesForUser_NoConversations() {
        String username = "user";
        Long user1_id = 1L;
        Parent parent1 = new Parent();
        parent1.setId(user1_id);
        when(userRepository.getByName(username)).thenReturn(Optional.of(parent1));
        when(conversationRepository.findAllConversationsByUserId(anyInt())).thenReturn(List.of());
        Response<List<MessageDTO>> response = conversationService.getLatestMessagesForUser(username);
        assertTrue(response.isSuccess());
        assertTrue(response.getData().isEmpty());
    }

    @Test
    void testGetLast100MessagesBetweenUsers_NoConversation() {
        String username1 = "user1";
        String username2 = "user2";
        Long user1_id = 1L;
        Long user2_id = 2L;
        Parent parent1 = new Parent();
        parent1.setId(user1_id);
        Parent parent2 = new Parent();
        parent2.setId(user2_id);
        when(userRepository.getByName(username1)).thenReturn(Optional.of(parent1));
        when(userRepository.getByName(username2)).thenReturn(Optional.of(parent2));
        when(conversationRepository.findConversationByUserIds(anyInt(), anyInt())).thenReturn(Optional.empty());
        Response<List<MessageDTO>> response = conversationService.getLast100MessagesBetweenUsers(username1, username2);
        assertFalse(response.isSuccess());
        assertEquals("No conversation found between the users.", response.getErrorMsg());
    }
}
