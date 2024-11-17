package com.example.main.response;

import com.example.main.dao.login.Baby;

import java.util.List;

public class LoginMessage {
    String message;
    Boolean status;
    private Long parentId;

    private List<Baby> babies;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public LoginMessage(String message, Boolean status) {
        this.message = message;
        this.status = status;
    }
    public LoginMessage(String message, Boolean status, Long parentId, List<Baby> babies) {
        this.message = message;
        this.status = status;
        this.parentId = parentId;
        this.babies = babies;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<Baby> getBabies() {
        return babies;
    }

    public void setBabies(List<Baby> babies) {
        this.babies = babies;
    }
}
