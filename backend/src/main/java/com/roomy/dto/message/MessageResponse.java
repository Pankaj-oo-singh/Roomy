package com.roomy.dto.message;

import com.roomy.dto.user.UserResponse;
import com.roomy.entity.Message;

import java.time.LocalDateTime;

public class MessageResponse {
    
    private Long id;
    private UserResponse sender;
    private UserResponse receiver;
    private String content;
    private boolean isRead;
    private LocalDateTime sentAt;
    
    // Constructor from Message entity
    public MessageResponse(Message message) {
        this.id = message.getId();
        this.sender = new UserResponse(message.getSender());
        this.receiver = new UserResponse(message.getReceiver());
        this.content = message.getContent();
        this.isRead = message.isRead();
        this.sentAt = message.getSentAt();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public UserResponse getSender() { return sender; }
    public void setSender(UserResponse sender) { this.sender = sender; }
    
    public UserResponse getReceiver() { return receiver; }
    public void setReceiver(UserResponse receiver) { this.receiver = receiver; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}