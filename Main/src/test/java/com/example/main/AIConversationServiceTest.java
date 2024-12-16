package com.example.main;

import com.example.main.converter.aiconv.AIMessageConverter;
import com.example.main.converter.aiconv.ConversationConverter;
import com.example.main.dao.aiconv.AIConversation;
import com.example.main.dao.aiconv.AIConversationRepository;
import com.example.main.dao.aiconv.AIMessage;
import com.example.main.dao.aiconv.AIMessageRepository;
import com.example.main.dao.login.Parent;
import com.example.main.dao.login.ParentRepository;
import com.example.main.dto.aiconv.AIMessageDTO;
import com.example.main.dto.aiconv.ConversationDTO;
import com.example.main.service.aiconv.AIConversationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        aiConversationService = new AIConversationService();
        aiConversationService.setRestTemplate(restTemplate);
        aiConversationService.setParentRepository(userRepository);
        aiConversationService.setAIConversationRepository(aiConversationRepository);
        aiConversationService.setAIMessageRepository(aiMessageRepository);
        aiConversationService.setConversationConverter(conversationConverter);
        aiConversationService.setAIMessageConverter(aiMessageConverter);
        aiConversationService.setAPIValue();
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
        AIConversation savedAIConversation = new AIConversation();
        savedAIConversation.setConversationId("test");
        when(aiConversationRepository.save(any(AIConversation.class))).thenReturn(savedAIConversation);

        Response<AIConversation> response = aiConversationService.createAIConversation(AIconvID, username, convname);
        assertTrue(response.isSuccess());
        assertEquals("test", response.getData().getConversationId());
    }

    @Test
    void testNewAIMessage() {

        String AIconvID = "test";
        String query = "宝宝哭了怎么办？";
        String answer = "可能是宝宝饿了。";
        AIMessage savedAIMessage = new AIMessage();
        savedAIMessage.setConversationId("test");

        AIMessageDTO savedAIMessageDTO = new AIMessageDTO();
        savedAIMessageDTO.setConv_id("test");

        when(aiMessageRepository.save(any(AIMessage.class))).thenReturn(savedAIMessage);
        when(aiMessageConverter.convertAIMessage(savedAIMessage)).thenReturn(savedAIMessageDTO);
        Response<AIMessageDTO> response = aiConversationService.newAIMessage(AIconvID, query, answer);
        assertTrue(response.isSuccess());
        assertEquals("test", response.getData().getConv_id());
    }

    @Test
    void testGetAIMessage_NotEmpty() {

        String username = "test";
        String query = "宝宝哭了怎么办？";
        String mode = "blocking";
        String AIconvID = "test";

        String body = "{\"event\": \"message\",\"message_id\": \"test_mess\",\"conversation_id\": \"test_conv\",\"mode\": \"advanced-chat\",\"answer\": \"Hello!\",\"metadata\": {},\"created_at\": 12345}";
        ResponseEntity<String> testResult = new ResponseEntity<String>(body, HttpStatus.OK);

        when(restTemplate.postForEntity(any(String.class),any(Object.class),eq(String.class))).thenReturn(testResult);

        AIMessage savedAIMessage = new AIMessage();
        savedAIMessage.setConversationId("test");
        AIMessageDTO savedAIMessageDTO = new AIMessageDTO();
        savedAIMessageDTO.setConv_id("test");
        savedAIMessageDTO.setAnswer("Hello!");

        when(aiMessageRepository.save(any(AIMessage.class))).thenReturn(savedAIMessage);
        when(aiMessageConverter.convertAIMessage(savedAIMessage)).thenReturn(savedAIMessageDTO);

        Response<AIMessageDTO> response = aiConversationService.getAIMessage(username, query, mode, AIconvID);
        assertTrue(response.isSuccess());
        assertEquals("test", response.getData().getConv_id());
        //assertEquals("Hello!", response.getData().getAnswer());
    }

    @Test
    void testGetAIMessage_Empty() {

        String username = "test";
        String query = "宝宝哭了怎么办？";
        String mode = "blocking";
        String AIconvID = "";

        String body = "{\"event\": \"message\",\"message_id\": \"test_mess\",\"conversation_id\": \"test_conv\",\"mode\": \"advanced-chat\",\"answer\": \"Hello!\",\"metadata\": {},\"created_at\": 12345}";
        ResponseEntity<String> testResult = new ResponseEntity<String>(body, HttpStatus.OK);

        when(restTemplate.postForEntity(any(String.class),any(Object.class),eq(String.class))).thenReturn(testResult);

        AIMessage savedAIMessage = new AIMessage();
        savedAIMessage.setConversationId("test");
        AIMessageDTO savedAIMessageDTO = new AIMessageDTO();
        savedAIMessageDTO.setConv_id("test_conv");
        savedAIMessageDTO.setAnswer("Hello!");

        when(aiMessageRepository.save(any(AIMessage.class))).thenReturn(savedAIMessage);
        when(aiMessageConverter.convertAIMessage(savedAIMessage)).thenReturn(savedAIMessageDTO);


        Long user_id = 1L;
        Parent parent = new Parent();
        parent.setId(user_id);
        when(userRepository.getByName(username)).thenReturn(Optional.of(parent));

        AIConversation savedAIConversation = new AIConversation();
        savedAIConversation.setConversationId("test_conv");
        when(aiConversationRepository.save(any(AIConversation.class))).thenReturn(savedAIConversation);

        Response<AIMessageDTO> response = aiConversationService.getAIMessage(username, query, mode, AIconvID);
        assertTrue(response.isSuccess());
        assertEquals("test_conv", response.getData().getConv_id());
        //assertEquals("Hello!", response.getData().getAnswer());
    }

    @Test
    void testGetAllConversation() {

        String username = "test";
        Long user_id = 1L;
        Parent parent = new Parent();
        parent.setId(user_id);
        when(userRepository.getByName(username)).thenReturn(Optional.of(parent));
        List<AIConversation> MyEmptyList = List.of();
        when(aiConversationRepository.findByUserId(user_id)).thenReturn(MyEmptyList);

        Response<List<ConversationDTO>> response = aiConversationService.getAllConversation(username);
        assertTrue(response.isSuccess());
        assertTrue(response.getData().isEmpty());
    }

    @Test
    void testGetAllMessage() {

        String AIconvID = "test";
        List<AIMessage> MyEmptyList = List.of();
        when(aiMessageRepository.findByConversationIdOrderByCreatedAtDesc(AIconvID)).thenReturn(MyEmptyList);

        Response<List<AIMessageDTO>> response = aiConversationService.getAllMessage(AIconvID);
        assertTrue(response.isSuccess());
        assertTrue(response.getData().isEmpty());
    }
}
