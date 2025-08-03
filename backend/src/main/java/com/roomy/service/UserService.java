package com.roomy.service;

import com.roomy.dto.user.UserProfileRequest;
import com.roomy.dto.user.UserResponse;
import com.roomy.entity.User;
import com.roomy.entity.VerificationStatus;
import com.roomy.exception.ResourceNotFoundException;
import com.roomy.repository.UserRepository;
import com.roomy.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        return UserPrincipal.create(user);
    }
    
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        return UserPrincipal.create(user);
    }
    
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Transactional
    public UserResponse updateProfile(Long userId, UserProfileRequest request) {
        User user = findById(userId);
        
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getLifestyle() != null) {
            user.setLifestyle(request.getLifestyle());
        }
        if (request.getSocialLinks() != null) {
            user.setSocialLinks(request.getSocialLinks());
        }
        
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }
    
    @Transactional
    public void uploadDocument(Long userId, String documentUrl) {
        User user = findById(userId);
        user.setDocumentUrl(documentUrl);
        user.setVerificationStatus(VerificationStatus.PENDING);
        userRepository.save(user);
    }
    
    @Transactional
    public void updateVerificationStatus(Long userId, VerificationStatus status) {
        User user = findById(userId);
        user.setVerificationStatus(status);
        userRepository.save(user);
    }
    
    public Page<UserResponse> getUsersWithPendingVerification(Pageable pageable) {
        return userRepository.findByVerificationStatus(VerificationStatus.PENDING, pageable)
                .map(UserResponse::new);
    }
    
    public UserResponse getUserProfile(Long userId) {
        User user = findById(userId);
        return new UserResponse(user);
    }
}