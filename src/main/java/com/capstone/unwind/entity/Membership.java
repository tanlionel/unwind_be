package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "membership")
public class Membership {
    @Id
    @Column(name = "membership_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "price")
    private Float price;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "is_active")
    private Boolean isActive;

}