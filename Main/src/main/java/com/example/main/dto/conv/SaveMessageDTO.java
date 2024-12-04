package com.example.main.dto.conv;

public class SaveMessageDTO {

    private String senderUsername;
    private String receiverUsername;
    private String content;

    public SaveMessageDTO(String senderUsername, String receiverUsername, String content) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.content = content;
    }

    public String getSenderUsername() {
        return senderUsername;
    }
    public String getReceiverUsername() {
        return receiverUsername;
    }
    public String getContent() {
        return content;
    }
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }
    public void setContent(String content) {
        this.content = content;
    }
}