package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "feedback")
public class Feedback {
    @Id
    @Column(name = "feedback_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rating_point")
    private Float ratingPoint;

    @Column(name = "comment", length = 300)
    private String comment;

    @Column(name = "note", length = 300)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resort_id")
    private Resort resort;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Customer user;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_report")
    private Boolean isReport;
    @PrePersist
    protected void onCreate() {
        this.createdDate = Timestamp.from(Instant.now());
    }



}