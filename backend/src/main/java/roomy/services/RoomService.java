package roomy.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import roomy.dto.room.RoomDto;
import roomy.entities.Room;
import roomy.entities.User;
import roomy.entities.enums.RoomStatus;
import roomy.exceptions.ResourceNotFoundException;
import roomy.repositories.RoomRepository;
import roomy.repositories.UserRepository;

import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoomRepository roomRepository;



    public RoomDto createRoom(RoomDto roomDto, User user) {
        Room room = modelMapper.map(roomDto, Room.class);
        room.setUser(user);
        room.setCreatedAt(LocalDateTime.now());
        Room savedRoom = roomRepository.save(room);
        return modelMapper.map(savedRoom, RoomDto.class);
    }



    public List<RoomDto> getRoomsByUser(User user) {
        List<Room> rooms = roomRepository.findByUser(user);
        return rooms.stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    public List<RoomDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }


    public RoomDto updateRoomStatus(Long roomId, RoomStatus status, User user) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        if (!room.getUser().getId().equals(user.getId())) {
           throw new AccessDeniedException("You are not authorized to update this room");
        }

        room.setStatus(status);
        Room updatedRoom = roomRepository.save(room);
        return modelMapper.map(updatedRoom, RoomDto.class);
    }

    public void deleteRoom(Long roomId, Long userId) {
        Room room = roomRepository.findByIdAndUserId(roomId, userId)
                .orElseThrow(() -> new AccessDeniedException("You are not authorized to delete this room"));

        roomRepository.delete(room);
    }


    public RoomDto uploadRoomImages(Long roomId, MultipartFile[] images, User user) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        if (!room.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to update this room");
        }

        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile file : images) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get("uploads/rooms/" + fileName);
            try {
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());
                uploadedUrls.add("/uploads/rooms/" + fileName); // relative path to access later
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        room.getImageUrls().addAll(uploadedUrls);
        Room saved = roomRepository.save(room);

        return modelMapper.map(saved, RoomDto.class);
    }


    public RoomDto updateRoom(Long roomId, RoomDto roomDto, User user) {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        // Check ownership
        if (!existingRoom.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to update this room");
        }

        // Update fields
        existingRoom.setTitle(roomDto.getTitle());
        existingRoom.setDescription(roomDto.getDescription());
        existingRoom.setPrice(roomDto.getPrice());
        existingRoom.setLocation(roomDto.getLocation());
        existingRoom.setImageUrls(roomDto.getImageUrls() != null ? new ArrayList<>(roomDto.getImageUrls()) : existingRoom.getImageUrls());
        existingRoom.setFurnished(roomDto.isFurnished());
        existingRoom.setAvailable(roomDto.isAvailable());
        existingRoom.setRoomType(roomDto.getRoomType());
        existingRoom.setStatus(roomDto.getStatus() != null ? roomDto.getStatus() : existingRoom.getStatus());
        existingRoom.setAvailableFrom(roomDto.getAvailableFrom());
        existingRoom.setGenderPreference(roomDto.getGenderPreference());
        existingRoom.setMaxOccupancy(roomDto.getMaxOccupancy());

        Room savedRoom = roomRepository.save(existingRoom);


        return modelMapper.map(savedRoom, RoomDto.class);
    }



    public RoomDto getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        return modelMapper.map(room, RoomDto.class);
    }

}