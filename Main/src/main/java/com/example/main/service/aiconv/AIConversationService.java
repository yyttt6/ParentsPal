package com.example.main.service.aiconv;

import com.example.main.converter.aiconv.AIMessageConverter;
import com.example.main.converter.aiconv.ConversationConverter;
import com.example.main.converter.conv.MessageConverter;
import com.example.main.dao.aiconv.*;
import com.example.main.dao.conv.ConversationRepository;
import com.example.main.dao.conv.MessageRepository;
import com.example.main.dao.login.Parent;
import com.example.main.dao.login.ParentRepository;
import com.example.main.dto.aiconv.AIMessageDTO;
import com.example.main.dto.aiconv.ConversationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.main.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class AIConversationService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ParentRepository userRepository;
    @Autowired
    private AIConversationRepository aiConversationRepository;
    @Autowired
    private AIMessageRepository aiMessageRepository;
    @Autowired
    private ConversationConverter conversationConverter;
    @Autowired
    private AIMessageConverter aiMessageConverter;

    @Value("${ai.api.key}")
    private String API_KEY;
    @Value("${ai.api.url}")
    private String API_URL;

    public Response<Long> getUserIdByUsername(String username) {

        if (username == null || username.isEmpty()) {
            return Response.newFail("Username cannot be null or empty");
        }

        Optional<Parent> userOptional = userRepository.getByName(username);
        return userOptional.map(user -> Response.newSuccess(user.getId()))
                .orElseGet(() -> Response.newFail("User not found with the given username"));
    }


    public Response<AIConversation> createAIConversation(String AIconvID, String username, String convname) {

        Response<Long> userIdResponse = getUserIdByUsername(username);
        Long user_id = (long) Math.toIntExact(userIdResponse.getData());

        AIConversation AIconv = new AIConversation();
        AIconv.setUserId(user_id);
        AIconv.setConversationId(AIconvID);
        AIconv.setConversationName(convname);
        AIconv.setCreatedAt(LocalDateTime.now());

        AIConversation savedAIConversation = aiConversationRepository.save(AIconv);
        return Response.newSuccess(savedAIConversation);
    }

    public Response<AIMessageDTO> newAIMessage(String AIconvID, String query, String answer) {

        AIMessage AImess = new AIMessage();
        AImess.setConversationId(AIconvID);
        AImess.setQuery(query);
        AImess.setAnswer(answer);
        AImess.setCreatedAt(LocalDateTime.now());

        AIMessage savedAIMessage = aiMessageRepository.save(AImess);
        return Response.newSuccess(aiMessageConverter.convertAIMessage(savedAIMessage));
    }

    public Response<AIMessageDTO> getAIMessage(String username, String query, String mode, String AIconvID) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("inputs", "");
        paraMap.put("query", query);
        paraMap.put("response_mode", mode);
        paraMap.put("conversation_id", AIconvID);
        paraMap.put("user", username);
        HttpEntity<Map<String, Object>>API_BODY = new HttpEntity<Map<String, Object>>(paraMap, headers);

        ResponseEntity<String> result = restTemplate.postForEntity(API_URL, API_BODY, String.class);
        String jsonResult = result.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResult);
            String convID = jsonNode.get("conversation_id").asText();
            String answer = jsonNode.get("answer").asText();

            if (AIconvID.isEmpty()) {
                Response<AIConversation> conversationResponse = createAIConversation(convID, username, query);
                if (!conversationResponse.isSuccess()) {
                    return Response.newFail(conversationResponse.getErrorMsg());
                }
            }

            Response<AIMessageDTO> messageResponse = newAIMessage(convID, query, answer);
            if (!messageResponse.isSuccess()) {
                return Response.newFail(messageResponse.getErrorMsg());
            }
            return messageResponse;

        } catch (JsonMappingException e) {
            System.err.println("Json Mapping Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            System.err.println("Json Processing Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return Response.newFail("AI failed.");
    }

    public Response<List<ConversationDTO>> getAllConversation(String username) {
        Response<Long> userIdResponse = getUserIdByUsername(username);
        if (!userIdResponse.isSuccess()) {
            return Response.newFail(userIdResponse.getErrorMsg());
        }
        Long user_id = (long)Math.toIntExact(userIdResponse.getData());
        List<AIConversation> conversations = aiConversationRepository.findByUserId(user_id);

        List<ConversationDTO> conversationHistory = conversations.stream()
                .map(conversationConverter::convertConversation)
                .collect(Collectors.toList());
        return Response.newSuccess(conversationHistory);
    }
    public Response<List<AIMessageDTO>> getAllMessage(String AIconvID) {
        List<AIMessage> messages = aiMessageRepository.findByConversationIdOrderByCreatedAtDesc(AIconvID);
        List<AIMessageDTO> messageHistory = messages.stream()
                .map(aiMessageConverter::convertAIMessage)
                .collect(Collectors.toList());
        return Response.newSuccess(messageHistory);
    }
    public void setRestTemplate(RestTemplate restTemplate) {this.restTemplate = restTemplate;}
    public void setParentRepository(ParentRepository userRepository) {this.userRepository = userRepository;}
    public void setAIConversationRepository(AIConversationRepository aiConversationRepository) {this.aiConversationRepository = aiConversationRepository;}
    public void setAIMessageRepository(AIMessageRepository aiMessageRepository) {this.aiMessageRepository = aiMessageRepository;}
    public void setConversationConverter(ConversationConverter conversationConverter) {this.conversationConverter = conversationConverter;}
    public void setAIMessageConverter(AIMessageConverter aiMessageConverter) {this.aiMessageConverter = aiMessageConverter;}
    public void setAPIValue() {this.API_URL = "http://localhost:80/v1/chat-messages"; this.API_KEY = "app-2Z15tg459MeUA12SSjKuoyYt";};
}
