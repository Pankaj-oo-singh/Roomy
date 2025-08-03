package com.roomy.service;

import com.roomy.dto.room.RoomRequest;
import com.roomy.dto.room.RoomResponse;
import com.roomy.entity.Room;
import com.roomy.entity.RoomStatus;
import com.roomy.entity.User;
import com.roomy.entity.VerificationStatus;
import com.roomy.exception.BadRequestException;
import com.roomy.exception.ResourceNotFoundException;
import com.roomy.exception.UnauthorizedException;
import com.roomy.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private UserService userService;
    
    @Transactional
    public RoomResponse createRoom(Long userId, RoomRequest request) {
        User user = userService.findById(userId);
        
        // Only verified users can post rooms
        if (user.getVerificationStatus() != VerificationStatus.APPROVED) {
            throw new UnauthorizedException("Only verified users can post rooms. Please complete your verification first.");
        }
        
        Room room = new Room();
        room.setTitle(request.getTitle());
        room.setDescription(request.getDescription());
        room.setLocation(request.getLocation());
        room.setRent(request.getRent());
        room.setAmenities(request.getAmenities());
        room.setPreferredRoommate(request.getPreferredRoommate());
        room.setAvailableFrom(request.getAvailableFrom());
        room.setAvailableTo(request.getAvailableTo());
        room.setOwner(user);
        room.setStatus(RoomStatus.AVAILABLE);
        
        Room savedRoom = roomRepository.save(room);
        return new RoomResponse(savedRoom);
    }
    
    @Transactional
    public RoomResponse updateRoom(Long userId, Long roomId, RoomRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));
        
        // Only room owner can update
        if (!room.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("You can only update your own rooms");
        }
        
        room.setTitle(request.getTitle());
        room.setDescription(request.getDescription());
        room.setLocation(request.getLocation());
        room.setRent(request.getRent());
        room.setAmenities(request.getAmenities());
        room.setPreferredRoommate(request.getPreferredRoommate());
        room.setAvailableFrom(request.getAvailableFrom());
        room.setAvailableTo(request.getAvailableTo());
        
        Room savedRoom = roomRepository.save(room);
        return new RoomResponse(savedRoom);
    }
    
    @Transactional
    public void deleteRoom(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));
        
        // Only room owner can delete
        if (!room.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("You can only delete your own rooms");
        }
        
        roomRepository.delete(room);
    }
    
    @Transactional
    public void updateRoomImages(Long userId, Long roomId, List<String> imageUrls) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));
        
        // Only room owner can update images
        if (!room.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("You can only update your own rooms");
        }
        
        room.setImageUrls(imageUrls);
        roomRepository.save(room);
    }
    
    public Page<RoomResponse> searchRooms(String location, BigDecimal minRent, BigDecimal maxRent, Pageable pageable) {
        return roomRepository.searchRooms(location, minRent, maxRent, pageable)
                .map(RoomResponse::new);
    }
    
    public Page<RoomResponse> getAvailableRooms(Pageable pageable) {
        return roomRepository.findByStatus(RoomStatus.AVAILABLE, pageable)
                .map(RoomResponse::new);
    }
    
    public Page<RoomResponse> getVerifiedRooms(Pageable pageable) {
        return roomRepository.findVerifiedRooms(pageable)
                .map(RoomResponse::new);
    }
    
    public RoomResponse getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));
        return new RoomResponse(room);
    }
    
    public List<RoomResponse> getUserRooms(Long userId) {
        User user = userService.findById(userId);
        return roomRepository.findByOwner(user).stream()
                .map(RoomResponse::new)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void updateRoomStatus(Long userId, Long roomId, RoomStatus status) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));
        
        // Only room owner can update status
        if (!room.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("You can only update your own rooms");
        }
        
        room.setStatus(status);
        roomRepository.save(room);
    }
}