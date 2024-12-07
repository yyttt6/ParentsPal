package com.example.main.dto.aiconv;

import java.time.LocalDateTime;

public class AIMessageDTO {
    private String conv_id;
    private String query;
    private String answer;
    private LocalDateTime created_at;

    public String getConv_id() { return conv_id; }

    public void setConv_id(String conv_id) {this.conv_id = conv_id; }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) { this.query = query; }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) { this.answer = answer; }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
