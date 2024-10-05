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
@Table(name = "room_info")
public class RoomInfo {
    @Id
    @Column(name = "room_info_id", nullable = false)
    private Integer id;

    @Column(name = "room_info_code", length = 45)
    private String roomInfoCode;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "resort_id")
    private Integer resortId;

    @Column(name = "status", length = 45)
    private String status;

    @Column(name = "unit_type_id")
    private Integer unitTypeId;

}