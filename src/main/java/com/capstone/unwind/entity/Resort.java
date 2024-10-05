package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "resort")
public class Resort {
    @Id
    @Column(name = "resort_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "resort_name", length = 45)
    private String resortName;

    @Column(name = "logo", length = 200)
    private String logo;

    @Column(name = "min_price")
    private Float minPrice;

    @Column(name = "max_price")
    private Float maxPrice;

    @Column(name = "status", length = 45)
    private String status;

    @Column(name = "address", length = 200)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeshare_company_id")
    private TimeshareCompany timeshareCompany;

    @Column(name = "is_active")
    private Boolean isActive;

}