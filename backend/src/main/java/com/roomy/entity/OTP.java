package com.roomy.entity;

import java.time.LocalDateTime;

public class OTP {
    
    private String email;
    private String code;
    private LocalDateTime expiryTime;
    
    public OTP() {}
    
    public OTP(String email, String code, LocalDateTime expiryTime) {
        this.email = email;
        this.code = code;
        this.expiryTime = expiryTime;
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }
    
    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public LocalDateTime getExpiryTime() { return expiryTime; }
    public void setExpiryTime(LocalDateTime expiryTime) { this.expiryTime = expiryTime; }
}