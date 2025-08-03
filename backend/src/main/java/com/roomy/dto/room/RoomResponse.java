package com.roomy.dto.room;

import com.roomy.dto.user.UserResponse;
import com.roomy.entity.Room;
import com.roomy.entity.RoomStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class RoomResponse {
    
    private Long id;
    private String title;
    private String description;
    private String location;
    private BigDecimal rent;
    private List<String> amenities;
    private Map<String, Object> preferredRoommate;
    private LocalDate availableFrom;
    private LocalDate availableTo;
    private List<String> imageUrls;
    private RoomStatus status;
    private UserResponse owner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor from Room entity
    public RoomResponse(Room room) {
        this.id = room.getId();
        this.title = room.getTitle();
        this.description = room.getDescription();
        this.location = room.getLocation();
        this.rent = room.getRent();
        this.amenities = room.getAmenities();
        this.preferredRoommate = room.getPreferredRoommate();
        this.availableFrom = room.getAvailableFrom();
        this.availableTo = room.getAvailableTo();
        this.imageUrls = room.getImageUrls();
        this.status = room.getStatus();
        this.owner = new UserResponse(room.getOwner());
        this.createdAt = room.getCreatedAt();
        this.updatedAt = room.getUpdatedAt();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public BigDecimal getRent() { return rent; }
    public void setRent(BigDecimal rent) { this.rent = rent; }
    
    public List<String> getAmenities() { return amenities; }
    public void setAmenities(List<String> amenities) { this.amenities = amenities; }
    
    public Map<String, Object> getPreferredRoommate() { return preferredRoommate; }
    public void setPreferredRoommate(Map<String, Object> preferredRoommate) { this.preferredRoommate = preferredRoommate; }
    
    public LocalDate getAvailableFrom() { return availableFrom; }
    public void setAvailableFrom(LocalDate availableFrom) { this.availableFrom = availableFrom; }
    
    public LocalDate getAvailableTo() { return availableTo; }
    public void setAvailableTo(LocalDate availableTo) { this.availableTo = availableTo; }
    
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    
    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
    
    public UserResponse getOwner() { return owner; }
    public void setOwner(UserResponse owner) { this.owner = owner; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}