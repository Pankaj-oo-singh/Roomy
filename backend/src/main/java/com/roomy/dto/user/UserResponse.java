package com.roomy.dto.user;

import com.roomy.entity.User;
import com.roomy.entity.VerificationStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class UserResponse {
    
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String bio;
    private Map<String, Object> lifestyle;
    private Map<String, String> socialLinks;
    private VerificationStatus verificationStatus;
    private LocalDateTime createdAt;
    
    // Constructor from User entity
    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.bio = user.getBio();
        this.lifestyle = user.getLifestyle();
        this.socialLinks = user.getSocialLinks();
        this.verificationStatus = user.getVerificationStatus();
        this.createdAt = user.getCreatedAt();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public Map<String, Object> getLifestyle() { return lifestyle; }
    public void setLifestyle(Map<String, Object> lifestyle) { this.lifestyle = lifestyle; }
    
    public Map<String, String> getSocialLinks() { return socialLinks; }
    public void setSocialLinks(Map<String, String> socialLinks) { this.socialLinks = socialLinks; }
    
    public VerificationStatus getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(VerificationStatus verificationStatus) { this.verificationStatus = verificationStatus; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}