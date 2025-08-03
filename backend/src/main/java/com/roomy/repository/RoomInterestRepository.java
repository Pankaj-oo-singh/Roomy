package com.roomy.repository;

import com.roomy.entity.Room;
import com.roomy.entity.RoomInterest;
import com.roomy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomInterestRepository extends JpaRepository<RoomInterest, Long> {
    
    Optional<RoomInterest> findByRoomAndUser(Room room, User user);
    
    List<RoomInterest> findByRoom(Room room);
    
    List<RoomInterest> findByUser(User user);
    
    boolean existsByRoomAndUser(Room room, User user);
}