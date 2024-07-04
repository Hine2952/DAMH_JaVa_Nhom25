package com.example.ChatWeb.request;

public class SenMessageRequest {
    private Integer userId;
    private Integer chatId;
    private String content;

    public SenMessageRequest() {
    }

    public SenMessageRequest(Integer userId, String content, Integer chatId) {
        this.userId = userId;
        this.content = content;
        this.chatId = chatId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
