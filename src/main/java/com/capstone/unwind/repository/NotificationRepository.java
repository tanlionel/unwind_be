package com.capstone.unwind.repository;

import com.capstone.unwind.entity.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Page<Notification> findAllByUserId(Integer userId, Pageable pageable);
    Page<Notification> findAllByRole(String role,Pageable pageable);



    @Modifying
    @Transactional
    @Query("UPDATE Notification n set n.isRead=true where n.userId =: userId")
    void markAllReadByUserId(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n set n.isRead=true where n.role =: topic")
    void markAllReadByTopic(@Param("topic") String topic);
}