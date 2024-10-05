package com.capstone.unwind.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "membership")
public class Membership {
    @Id
    @Column(name = "membership_id", nullable = false)
    private Integer id;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "price")
    private Float price;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "type", length = 45)
    private String type;

    @Column(name = "is_active")
    private Boolean isActive;

}