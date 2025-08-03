package com.roomy.service;

import com.roomy.dto.message.MessageRequest;
import com.roomy.dto.message.MessageResponse;
import com.roomy.entity.Message;
import com.roomy.entity.User;
import com.roomy.exception.ResourceNotFoundException;
import com.roomy.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserService userService;
    
    @Transactional
    public MessageResponse sendMessage(Long senderId, MessageRequest request) {
        User sender = userService.findById(senderId);
        User receiver = userService.findById(request.getReceiverId());
        
        Message message = new Message(sender, receiver, request.getContent());
        Message savedMessage = messageRepository.save(message);
        
        return new MessageResponse(savedMessage);
    }
    
    public List<MessageResponse> getConversation(Long userId, Long otherUserId) {
        User user = userService.findById(userId);
        User otherUser = userService.findById(otherUserId);
        
        return messageRepository.findConversation(user, otherUser).stream()
                .map(MessageResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<User> getConversationPartners(Long userId) {
        User user = userService.findById(userId);
        return messageRepository.findConversationPartners(user);
    }
    
    @Transactional
    public void markMessageAsRead(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));
        
        // Only receiver can mark message as read
        if (!message.getReceiver().getId().equals(userId)) {
            throw new RuntimeException("You can only mark your received messages as read");
        }
        
        message.setRead(true);
        messageRepository.save(message);
    }
    
    public long getUnreadMessageCount(Long userId) {
        User user = userService.findById(userId);
        return messageRepository.countUnreadMessages(user);
    }
    
    public Page<MessageResponse> getUnreadMessages(Long userId, Pageable pageable) {
        User user = userService.findById(userId);
        return messageRepository.findByReceiverAndIsReadFalse(user, pageable)
                .map(MessageResponse::new);
    }
}