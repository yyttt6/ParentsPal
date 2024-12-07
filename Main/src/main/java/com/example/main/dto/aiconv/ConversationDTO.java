package com.example.main.dto.aiconv;

import java.time.LocalDateTime;

public class ConversationDTO {
    private String conv_id;
    private String conv_name;
    private LocalDateTime created_at;

    public String getConv_id() { return conv_id; }

    public void setConv_id(String conv_id) {this.conv_id = conv_id; }

    public String getConv_name() {
        return conv_name;
    }

    public void setConv_name(String conv_name) {
        this.conv_name = conv_name;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}