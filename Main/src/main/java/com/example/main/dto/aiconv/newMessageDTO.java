package com.example.main.dto.aiconv;

public class newMessageDTO {
    private String conversationId;
    private String query;
    private String mode;

    public newMessageDTO(String conversationId, String query, String mode) {
        this.conversationId = conversationId;
        this.query = query;
        this.mode = mode;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

}