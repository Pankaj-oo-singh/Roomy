package com.roomy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendOTPEmail(String to, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Roomy - Your OTP Code");
        message.setText("Your OTP code is: " + otpCode + "\n\nThis code will expire in 5 minutes.\n\nIf you didn't request this code, please ignore this email.");
        
        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
    
    public void sendVerificationUpdateEmail(String to, String status, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Roomy - Verification Status Update");
        
        String text = String.format(
            "Hello %s,\n\nYour document verification status has been updated to: %s\n\n" +
            "If approved, you can now post rooms on Roomy!\n\nThank you for using Roomy.",
            username, status
        );
        
        message.setText(text);
        
        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}