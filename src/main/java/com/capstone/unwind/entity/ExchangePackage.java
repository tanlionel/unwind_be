package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "exchange_package")
public class ExchangePackage {
    @Id
    @Column(name = "exchange_package_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "package_name", length = 45)
    private String packageName;

    @Column(name = "package_type", length = 45)
    private String packageType;

    @Column(name = "price")
    private Float price;

    @Column(name = "commission_rate")
    private Float commissionRate;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "description", length = 45)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

}