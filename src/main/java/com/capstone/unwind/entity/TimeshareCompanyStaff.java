package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "timeshare_company_staff")
public class TimeshareCompanyStaff {
    @Id
    @Column(name = "timeshare_company_staff_id", nullable = false)
    private Integer id;

    @Column(name = "user_name", length = 45)
    private String userName;

    @Column(name = "password", length = 100)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeshare_company_id")
    private TimeshareCompany timeshareCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resort_id")
    private Resort resort;

    @Column(name = "is_active")
    private Boolean isActive;

}