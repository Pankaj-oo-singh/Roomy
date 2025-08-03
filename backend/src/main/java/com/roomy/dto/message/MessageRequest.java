package com.roomy.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MessageRequest {
    
    @NotNull(message = "Receiver ID is required")
    private Long receiverId;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    // Constructors
    public MessageRequest() {}
    
    public MessageRequest(Long receiverId, String content) {
        this.receiverId = receiverId;
        this.content = content;
    }
    
    // Getters and Setters
    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}