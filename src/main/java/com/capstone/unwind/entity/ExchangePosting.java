package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "exchange_posting")
public class ExchangePosting {
    @Id
    @Column(name = "exchange_posting_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nights")
    private Integer nights;

    @Column(name = "is_verify")
    private Boolean isVerify;

    @Column(name = "is_exchange")
    private Boolean isExchange;

    @Column(name = "status", length = 45)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_package_id")
    private ExchangePackage exchangePackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Customer owner;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "checkin_date")
    private LocalDate checkinDate;

    @Column(name = "checkout_date")
    private LocalDate checkoutDate;

    @Column(name = "prefer_checkin_date")
    private LocalDate preferCheckinDate;

    @Column(name = "prefer_checkout_date")
    private LocalDate preferCheckoutDate;

    @Column(name = "prefer_location", length = 45)
    private String preferLocation;

    @Column(name = "note", length = 200)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeshare_id")
    private Timeshare timeshare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_info_id")
    private RoomInfo roomInfo;

    @Column(name = "expired")
    private LocalDate expired;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;
    @PrePersist
    protected void onCreate() {
        this.createdDate = Timestamp.from(Instant.now());
        this.updatedDate = Timestamp.from(Instant.now());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = Timestamp.from(Instant.now());
    }

}