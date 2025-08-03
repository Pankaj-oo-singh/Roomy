package com.roomy.controller;

import com.roomy.dto.auth.*;
import com.roomy.service.AuthService;
import com.roomy.service.UserService;
import com.roomy.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        JwtResponse response = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse response = authService.loginUser(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOTP(@Valid @RequestBody OTPRequest otpRequest) {
        authService.sendOTP(otpRequest.getEmail());
        return ResponseEntity.ok(Map.of("message", "OTP sent to your email"));
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<JwtResponse> verifyOTP(@Valid @RequestBody OTPVerifyRequest request) {
        JwtResponse response = authService.verifyOTPAndLogin(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        authService.changePassword(currentUser.getId(), request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }
    
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(Map.of(
            "id", currentUser.getId(),
            "username", currentUser.getUsername(),
            "email", currentUser.getEmail()
        ));
    }
}