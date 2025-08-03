package com.roomy.repository;

import com.roomy.entity.Message;
import com.roomy.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender = :user1 AND m.receiver = :user2) OR " +
           "(m.sender = :user2 AND m.receiver = :user1) " +
           "ORDER BY m.sentAt ASC")
    List<Message> findConversation(@Param("user1") User user1, @Param("user2") User user2);
    
    @Query("SELECT DISTINCT CASE WHEN m.sender = :user THEN m.receiver ELSE m.sender END " +
           "FROM Message m WHERE m.sender = :user OR m.receiver = :user")
    List<User> findConversationPartners(@Param("user") User user);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver = :user AND m.isRead = false")
    long countUnreadMessages(@Param("user") User user);
    
    Page<Message> findByReceiverAndIsReadFalse(User receiver, Pageable pageable);
}