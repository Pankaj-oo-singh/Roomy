package com.roomy.repository;

import com.roomy.entity.Room;
import com.roomy.entity.RoomStatus;
import com.roomy.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    Page<Room> findByStatus(RoomStatus status, Pageable pageable);
    
    List<Room> findByOwner(User owner);
    
    @Query("SELECT r FROM Room r WHERE r.status = 'AVAILABLE' " +
           "AND (:location IS NULL OR LOWER(r.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
           "AND (:minRent IS NULL OR r.rent >= :minRent) " +
           "AND (:maxRent IS NULL OR r.rent <= :maxRent)")
    Page<Room> searchRooms(@Param("location") String location,
                          @Param("minRent") BigDecimal minRent,
                          @Param("maxRent") BigDecimal maxRent,
                          Pageable pageable);
    
    @Query("SELECT r FROM Room r WHERE r.owner.verificationStatus = 'APPROVED' AND r.status = 'AVAILABLE'")
    Page<Room> findVerifiedRooms(Pageable pageable);
}