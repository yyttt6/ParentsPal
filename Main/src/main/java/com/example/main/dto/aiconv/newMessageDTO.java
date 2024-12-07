package com.example.main.dto.aiconv;

public class newMessageDTO {
    private String username;
    private String conversationId;
    private String query;
    private String mode;

    public newMessageDTO(String username, String conversationId, String query, String mode) {
        this.username = username;
        this.conversationId = conversationId;
        this.query = query;
        this.mode = mode;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { this.username = username; }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) { this.query = query; }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) { this.mode = mode; }

}