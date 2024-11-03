package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customer")
public class Customer {
    @Id
    @Column(name = "customer_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name", length = 45)
    private String fullName;

    @Column(name = "avatar", length = 200)
    private String avatar;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "phone", length = 12)
    private String phone;

    @Column(name = "member_purchase_date")
    private LocalDate memberPurchaseDate;

    @Column(name = "member_expiry_date")
    private LocalDate memberExpiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id")
    private Membership membership;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "street", length = 100)
    private String street;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "postal_code", length = 45)
    private String postalCode;

    @Column(name = "note", length = 300)
    private String note;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",unique = true)
    private User user;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne(mappedBy = "owner", fetch = FetchType.LAZY)
    private Wallet wallet;

}