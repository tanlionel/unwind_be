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
@Table(name = "rental_posting")
public class RentalPosting {
    @Id
    @Column(name = "rental_posting_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "nights")
    private Integer nights;

    @Column(name = "price_per_nights")
    private Float pricePerNights;

    @Column(name = "is_verify")
    private Boolean isVerify;

    @Column(name = "is_bookable")
    private Boolean isBookable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeshare_id")
    private Timeshare timeshare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_info_id")
    private RoomInfo roomInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancellation_type")
    private CancelationPolicy cancellationType;

    @Column(name = "checkin_date")
    private LocalDate checkinDate;

    @Column(name = "checkout_date")
    private LocalDate checkoutDate;

    @Column(name = "expired_date")
    private LocalDate expiredDate;

    @Column(name = "status", length = 45)
    private String status;

    @Column(name = "staff_refinement_price")
    private Float staffRefinementPrice;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_package_id")
    private RentalPackage rentalPackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Customer owner;

    @Column(name = "note", length = 200)
    private String note;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @Column(name = "price_valuation")
    private Float priceValuation;

}