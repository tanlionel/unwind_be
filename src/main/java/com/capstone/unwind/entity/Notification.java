package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", length = 45)
    private String title;

    @Column(name = "image", length = 300)
    private String image;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead ;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "type", length = 20)
    private String type;

    @Column(name = "role", length = 20)
    private String role;

}
