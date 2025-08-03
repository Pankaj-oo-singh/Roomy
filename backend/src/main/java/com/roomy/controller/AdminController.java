package com.roomy.controller;

import com.roomy.dto.user.UserResponse;
import com.roomy.entity.VerificationStatus;
import com.roomy.service.EmailService;
import com.roomy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private EmailService emailService;
    
    @GetMapping("/users/pending-verification")
    public ResponseEntity<Page<UserResponse>> getPendingVerificationUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> users = userService.getUsersWithPendingVerification(pageable);
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/users/{userId}/verification")
    public ResponseEntity<Map<String, String>> updateVerificationStatus(
            @PathVariable Long userId,
            @RequestParam VerificationStatus status) {
        userService.updateVerificationStatus(userId, status);
        
        // Send email notification
        try {
            UserResponse user = userService.getUserProfile(userId);
            emailService.sendVerificationUpdateEmail(user.getEmail(), status.name(), user.getUsername());
        } catch (Exception e) {
            // Log error but don't fail the request
            System.err.println("Failed to send verification email: " + e.getMessage());
        }
        
        return ResponseEntity.ok(Map.of("message", "Verification status updated successfully"));
    }
    
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        UserResponse user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }
}