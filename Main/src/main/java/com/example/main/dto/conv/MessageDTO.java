package com.example.main.dto.conv;

import java.time.LocalDateTime;

public class MessageDTO {
    private String sender_name;
    private String receiver_name;
    private String content;
    private LocalDateTime created_at;
    private Long sender_id;
    private Long receiver_id;
    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    public Long getSender_id() {
        return sender_id;
    }
    public void setSender_id(Long sender_id) {
        this.sender_id = sender_id;
    }
    public Long getReceiver_id() {
        return receiver_id;
    }
    public void setReceiver_id(Long receiver_id) {
        this.receiver_id = receiver_id;
    }

}
