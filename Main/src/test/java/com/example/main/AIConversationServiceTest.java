package com.example.main;

import com.example.main.converter.aiconv.AIMessageConverter;
import com.example.main.converter.aiconv.ConversationConverter;
import com.example.main.dao.aiconv.AIConversation;
import com.example.main.dao.aiconv.AIConversationRepository;
import com.example.main.dao.aiconv.AIMessage;
import com.example.main.dao.aiconv.AIMessageRepository;
import com.example.main.dao.login.Parent;
import com.example.main.dao.login.ParentRepository;
import com.example.main.service.aiconv.AIConversationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AIConversationServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ParentRepository userRepository;
    @Mock
    private AIConversationRepository aiConversationRepository;
    @Mock
    private AIMessageRepository aiMessageRepository;
    @Mock
    private ConversationConverter conversationConverter;
    @Mock
    private AIMessageConverter aiMessageConverter;


    private AIConversationService aiConversationService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        aiConversationService.setRestTemplate(restTemplate);
        aiConversationService.setParentRepository(userRepository);
        aiConversationService.setAIConversationRepository(aiConversationRepository);
        aiConversationService.setAIMessageRepository(aiMessageRepository);
        aiConversationService.setConversationConverter(conversationConverter);
        aiConversationService.setAIMessageConverter(aiMessageConverter);
    }
    @Test
    void testGetUserIdByUsername_UserNotFound() {
        String username = "testuser";
        when(userRepository.getByName(username)).thenReturn(Optional.empty());
        Response<Long> response = aiConversationService.getUserIdByUsername(username);
        assertFalse(response.isSuccess());
        assertEquals("User not found with the given username", response.getErrorMsg());
    }

    @Test
    void testGetUserIdByUsername_Success() {
        String username = "testuser";
        Parent parent = new Parent();
        parent.setId(1L);
        when(userRepository.getByName(username)).thenReturn(Optional.of(parent));
        Response<Long> response = aiConversationService.getUserIdByUsername(username);
        assertTrue(response.isSuccess());
        assertEquals(1L, response.getData());
    }

    @Test
    void testCreateAIConversation() {
        String AIconvID = "test";
        String username = "user1";
        String convname = "宝宝哭了怎么办？";
        Long user_id = 1L;
        Parent parent = new Parent();
        parent.setId(user_id);
        when(userRepository.getByName(username)).thenReturn(Optional.of(parent));

        Response<AIConversation> response = aiConversationService.createAIConversation(AIconvID, username, convname);
        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().getUserId());
    }
}
