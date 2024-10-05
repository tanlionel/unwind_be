package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "timeshare_company")
public class TimeshareCompany {
    @Id
    @Column(name = "timeshare_company_id", nullable = false)
    private Integer id;

    @Column(name = "timeshare_company_name", length = 45)
    private String timeshareCompanyName;

    @Column(name = "logo", length = 200)
    private String logo;

    @Column(name = "address", length = 300)
    private String address;

    @Column(name = "description", length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Customer owner;

    @Column(name = "contact", length = 45)
    private String contact;

    @Column(name = "is_active")
    private Boolean isActive;

}