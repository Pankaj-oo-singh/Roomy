package com.roomy.controller;

import com.roomy.dto.message.MessageRequest;
import com.roomy.dto.message.MessageResponse;
import com.roomy.entity.User;
import com.roomy.security.UserPrincipal;
import com.roomy.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(
            @Valid @RequestBody MessageRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        MessageResponse message = messageService.sendMessage(currentUser.getId(), request);
        return ResponseEntity.ok(message);
    }
    
    @GetMapping("/conversation/{userId}")
    public ResponseEntity<List<MessageResponse>> getConversation(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<MessageResponse> messages = messageService.getConversation(currentUser.getId(), userId);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/conversations")
    public ResponseEntity<List<User>> getConversationPartners(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<User> partners = messageService.getConversationPartners(currentUser.getId());
        return ResponseEntity.ok(partners);
    }
    
    @PutMapping("/{messageId}/read")
    public ResponseEntity<Map<String, String>> markAsRead(
            @PathVariable Long messageId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        messageService.markMessageAsRead(messageId, currentUser.getId());
        return ResponseEntity.ok(Map.of("message", "Message marked as read"));
    }
    
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@AuthenticationPrincipal UserPrincipal currentUser) {
        long count = messageService.getUnreadMessageCount(currentUser.getId());
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }
    
    @GetMapping("/unread")
    public ResponseEntity<Page<MessageResponse>> getUnreadMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageResponse> messages = messageService.getUnreadMessages(currentUser.getId(), pageable);
        return ResponseEntity.ok(messages);
    }
}