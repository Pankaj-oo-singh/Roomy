package com.roomy.repository;

import com.roomy.entity.User;
import com.roomy.entity.VerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsername(String username);
    
    boolean existsByEmail(String email);
    
    boolean existsByUsername(String username);
    
    @Query("SELECT u FROM User u WHERE u.verificationStatus = :status")
    Page<User> findByVerificationStatus(VerificationStatus status, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.documentUrl IS NOT NULL AND u.verificationStatus = 'PENDING'")
    List<User> findUsersWithPendingVerification();
}