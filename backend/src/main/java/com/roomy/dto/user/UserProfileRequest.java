package com.roomy.dto.user;

import jakarta.validation.constraints.Size;
import java.util.Map;

public class UserProfileRequest {
    
    private String fullName;
    
    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;
    
    private Map<String, Object> lifestyle;
    
    private Map<String, String> socialLinks;
    
    // Constructors
    public UserProfileRequest() {}
    
    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public Map<String, Object> getLifestyle() { return lifestyle; }
    public void setLifestyle(Map<String, Object> lifestyle) { this.lifestyle = lifestyle; }
    
    public Map<String, String> getSocialLinks() { return socialLinks; }
    public void setSocialLinks(Map<String, String> socialLinks) { this.socialLinks = socialLinks; }
}