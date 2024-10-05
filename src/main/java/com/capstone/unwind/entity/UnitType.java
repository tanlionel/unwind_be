package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "unit_type")
public class UnitType {
    @Id
    @Column(name = "unit_type_id", nullable = false)
    private Integer id;

    @Column(name = "title", length = 45)
    private String title;

    @Column(name = "area", length = 45)
    private String area;

    @Column(name = "bathrooms")
    private Integer bathrooms;

    @Column(name = "bedrooms")
    private Integer bedrooms;

    @Column(name = "beds_full")
    private Integer bedsFull;

    @Column(name = "beds_king")
    private Integer bedsKing;

    @Column(name = "beds_sofa")
    private Integer bedsSofa;

    @Column(name = "beds_murphy")
    private Integer bedsMurphy;

    @Column(name = "beds_queen")
    private Integer bedsQueen;

    @Column(name = "beds_twin")
    private Integer bedsTwin;

    @Column(name = "buildings_option", length = 45)
    private String buildingsOption;

    @Column(name = "price")
    private Float price;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "kitchen", length = 100)
    private String kitchen;

    @Column(name = "photos", length = 100)
    private String photos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resort_id")
    private Resort resort;

    @Column(name = "sleeps")
    private Integer sleeps;

    @Column(name = "view", length = 45)
    private String view;

    @Column(name = "is_active")
    private Boolean isActive;

}