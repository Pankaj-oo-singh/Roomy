package com.roomy.controller;

import com.roomy.dto.room.RoomRequest;
import com.roomy.dto.room.RoomResponse;
import com.roomy.entity.RoomStatus;
import com.roomy.security.UserPrincipal;
import com.roomy.service.CloudinaryService;
import com.roomy.service.RoomInterestService;
import com.roomy.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private RoomInterestService roomInterestService;
    
    @Autowired
    private CloudinaryService cloudinaryService;
    
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @Valid @RequestBody RoomRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        RoomResponse room = roomService.createRoom(currentUser.getId(), request);
        return ResponseEntity.ok(room);
    }
    
    @GetMapping
    public ResponseEntity<Page<RoomResponse>> getRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal minRent,
            @RequestParam(required = false) BigDecimal maxRent,
            @RequestParam(defaultValue = "false") boolean verifiedOnly) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RoomResponse> rooms;
        if (location != null || minRent != null || maxRent != null) {
            rooms = roomService.searchRooms(location, minRent, maxRent, pageable);
        } else if (verifiedOnly) {
            rooms = roomService.getVerifiedRooms(pageable);
        } else {
            rooms = roomService.getAvailableRooms(pageable);
        }
        
        return ResponseEntity.ok(rooms);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        RoomResponse room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        RoomResponse room = roomService.updateRoom(currentUser.getId(), id, request);
        return ResponseEntity.ok(room);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRoom(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        roomService.deleteRoom(currentUser.getId(), id);
        return ResponseEntity.ok(Map.of("message", "Room deleted successfully"));
    }
    
    @PostMapping("/{id}/images")
    public ResponseEntity<Map<String, Object>> uploadRoomImages(
            @PathVariable Long id,
            @RequestParam("files") MultipartFile[] files,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        try {
            List<String> imageUrls = new ArrayList<>();
            
            for (MultipartFile file : files) {
                String imageUrl = cloudinaryService.uploadRoomImage(file, id);
                imageUrls.add(imageUrl);
            }
            
            roomService.updateRoomImages(currentUser.getId(), id, imageUrls);
            
            return ResponseEntity.ok(Map.of(
                "message", "Images uploaded successfully",
                "imageUrls", imageUrls
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to upload images: " + e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/interest")
    public ResponseEntity<Map<String, String>> showInterest(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        roomInterestService.showInterest(currentUser.getId(), id);
        return ResponseEntity.ok(Map.of("message", "Interest shown successfully"));
    }
    
    @DeleteMapping("/{id}/interest")
    public ResponseEntity<Map<String, String>> removeInterest(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        roomInterestService.removeInterest(currentUser.getId(), id);
        return ResponseEntity.ok(Map.of("message", "Interest removed successfully"));
    }
    
    @GetMapping("/my-rooms")
    public ResponseEntity<List<RoomResponse>> getMyRooms(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<RoomResponse> rooms = roomService.getUserRooms(currentUser.getId());
        return ResponseEntity.ok(rooms);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> updateRoomStatus(
            @PathVariable Long id,
            @RequestParam RoomStatus status,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        roomService.updateRoomStatus(currentUser.getId(), id, status);
        return ResponseEntity.ok(Map.of("message", "Room status updated successfully"));
    }
}