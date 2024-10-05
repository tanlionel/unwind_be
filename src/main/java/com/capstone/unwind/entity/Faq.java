package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "faq")
public class Faq {
    @Id
    @Column(name = "faq_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", length = 45)
    private String type;

    @Column(name = "title", length = 45)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "created_date")
    private Timestamp createdDate;

}