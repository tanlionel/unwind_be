package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rental_package")
public class RentalPackage {
    @Id
    @Column(name = "rental_package_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rental_package_name", length = 50)
    private String rentalPackageName;

    @Column(name = "type", length = 45)
    private String type;

    @Column(name = "price")
    private Float price;

    @Column(name = "commission_rate")
    private Float commissionRate;

    @Column(name = "description", length = 45)
    private String description;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "is_active")
    private Boolean isActive;

}