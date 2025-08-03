package com.roomy.controller;

import com.roomy.dto.user.UserProfileRequest;
import com.roomy.dto.user.UserResponse;
import com.roomy.security.UserPrincipal;
import com.roomy.service.CloudinaryService;
import com.roomy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CloudinaryService cloudinaryService;
    
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
        UserResponse profile = userService.getUserProfile(currentUser.getId());
        return ResponseEntity.ok(profile);
    }
    
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(
            @Valid @RequestBody UserProfileRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        UserResponse updatedProfile = userService.updateProfile(currentUser.getId(), request);
        return ResponseEntity.ok(updatedProfile);
    }
    
    @PostMapping("/upload-document")
    public ResponseEntity<Map<String, String>> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        try {
            String documentUrl = cloudinaryService.uploadDocument(file, currentUser.getId());
            userService.uploadDocument(currentUser.getId(), documentUrl);
            
            return ResponseEntity.ok(Map.of(
                "message", "Document uploaded successfully",
                "documentUrl", documentUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to upload document: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse user = new UserResponse(userService.findByUsername(username));
        return ResponseEntity.ok(user);
    }
}