package com.capstone.unwind.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "activity_log")
public class ActivityLog {
    @Id
    @Column(name = "activity_log_id", nullable = false)
    private Integer id;

    @Column(name = "type", length = 45)
    private String type;

    @Column(name = "post_id", length = 45)
    private String postId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "note", length = 1000)
    private String note;

}