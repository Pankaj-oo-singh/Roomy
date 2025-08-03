package com.roomy.service;

import com.roomy.dto.auth.JwtResponse;
import com.roomy.dto.auth.LoginRequest;
import com.roomy.dto.auth.RegisterRequest;
import com.roomy.entity.OTP;
import com.roomy.entity.User;
import com.roomy.exception.BadRequestException;
import com.roomy.repository.UserRepository;
import com.roomy.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private EmailService emailService;
    
    @Value("${otp.expiration}")
    private long otpExpirationMs;
    
    // In-memory OTP store
    private final Map<String, OTP> otpStore = new ConcurrentHashMap<>();
    
    @Transactional
    public JwtResponse registerUser(RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }
        
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use!");
        }
        
        // Create new user
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFullName()
        );
        
        User result = userRepository.save(user);
        
        // Generate JWT token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signUpRequest.getEmail(),
                        signUpRequest.getPassword()
                )
        );
        
        String jwt = tokenProvider.generateToken(authentication);
        
        return new JwtResponse(jwt, result.getUsername(), result.getEmail(), result.getRole().name());
    }
    
    public JwtResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));
        
        return new JwtResponse(jwt, user.getUsername(), user.getEmail(), user.getRole().name());
    }
    
    public void sendOTP(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new BadRequestException("No account found with this email address");
        }
        
        String otpCode = generateOTPCode();
        LocalDateTime expiryTime = LocalDateTime.now().plusSeconds(otpExpirationMs / 1000);
        
        OTP otp = new OTP(email, otpCode, expiryTime);
        otpStore.put(email, otp);
        
        // Send OTP via email
        emailService.sendOTPEmail(email, otpCode);
    }
    
    public JwtResponse verifyOTPAndLogin(String email, String otpCode) {
        OTP storedOTP = otpStore.get(email);
        
        if (storedOTP == null) {
            throw new BadRequestException("No OTP found for this email. Please request a new one.");
        }
        
        if (storedOTP.isExpired()) {
            otpStore.remove(email);
            throw new BadRequestException("OTP has expired. Please request a new one.");
        }
        
        if (!storedOTP.getCode().equals(otpCode)) {
            throw new BadRequestException("Invalid OTP code");
        }
        
        // Remove OTP after successful verification
        otpStore.remove(email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
        
        String jwt = tokenProvider.generateTokenFromUserId(user.getId());
        
        return new JwtResponse(jwt, user.getUsername(), user.getEmail(), user.getRole().name());
    }
    
    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    private String generateOTPCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}