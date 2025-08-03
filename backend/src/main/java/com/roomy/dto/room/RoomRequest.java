package com.roomy.dto.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class RoomRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotNull(message = "Rent is required")
    @Positive(message = "Rent must be positive")
    private BigDecimal rent;
    
    private List<String> amenities;
    
    private Map<String, Object> preferredRoommate;
    
    @NotNull(message = "Available from date is required")
    private LocalDate availableFrom;
    
    private LocalDate availableTo;
    
    // Constructors
    public RoomRequest() {}
    
    // Getters and Setters
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
}