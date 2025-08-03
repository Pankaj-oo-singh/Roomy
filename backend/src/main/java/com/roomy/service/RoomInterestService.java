package com.roomy.service;

import com.roomy.entity.Room;
import com.roomy.entity.RoomInterest;
import com.roomy.entity.User;
import com.roomy.exception.BadRequestException;
import com.roomy.exception.ResourceNotFoundException;
import com.roomy.repository.RoomInterestRepository;
import com.roomy.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomInterestService {
    
    @Autowired
    private RoomInterestRepository roomInterestRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private UserService userService;
    
    @Transactional
    public void showInterest(Long userId, Long roomId) {
        User user = userService.findById(userId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));
        
        // Check if user already showed interest
        if (roomInterestRepository.existsByRoomAndUser(room, user)) {
            throw new BadRequestException("You have already shown interest in this room");
        }
        
        // Don't allow showing interest in own room
        if (room.getOwner().getId().equals(userId)) {
            throw new BadRequestException("You cannot show interest in your own room");
        }
        
        RoomInterest interest = new RoomInterest(room, user);
        roomInterestRepository.save(interest);
    }
    
    @Transactional
    public void removeInterest(Long userId, Long roomId) {
        User user = userService.findById(userId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));
        
        RoomInterest interest = roomInterestRepository.findByRoomAndUser(room, user)
                .orElseThrow(() -> new ResourceNotFoundException("Interest not found"));
        
        roomInterestRepository.delete(interest);
    }
    
    public List<User> getRoomInterestedUsers(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));
        
        return roomInterestRepository.findByRoom(room).stream()
                .map(RoomInterest::getUser)
                .collect(Collectors.toList());
    }
    
    public List<Room> getUserInterestedRooms(Long userId) {
        User user = userService.findById(userId);
        
        return roomInterestRepository.findByUser(user).stream()
                .map(RoomInterest::getRoom)
                .collect(Collectors.toList());
    }
}