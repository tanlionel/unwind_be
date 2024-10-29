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
@Table(name = "exchange_booking")
public class ExchangeBooking {
    @Id
    @Column(name = "exchange_booking_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeshare_id")
    private Timeshare timeshare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_info_id")
    private RoomInfo roomInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id")
    private Customer renter;

    @Column(name = "status", length = 45)
    private String status;

    @Column(name = "checkin_date")
    private LocalDate checkinDate;

    @Column(name = "checkout_date")
    private LocalDate checkoutDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_posting_id")
    private ExchangePosting exchangePosting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_request_id")
    private ExchangeRequest exchangeRequest;

    @Column(name = "type", length = 45)
    private String type;

    @Column(name = "primary_guest_name", length = 45)
    private String primaryGuestName;

    @Column(name = "primary_guest_phone", length = 45)
    private String primaryGuestPhone;

    @Column(name = "primary_guest_email", length = 45)
    private String primaryGuestEmail;

    @Column(name = "primary_guest_country", length = 45)
    private String primaryGuestCountry;

    @Column(name = "primary_guest_street", length = 45)
    private String primaryGuestStreet;

    @Column(name = "primary_guest_city", length = 45)
    private String primaryGuestCity;

    @Column(name = "primary_guest_state", length = 45)
    private String primaryGuestState;

    @Column(name = "primary_guest_postal_code", length = 45)
    private String primaryGuestPostalCode;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "renter_full_legal_name", length = 45)
    private String renterFullLegalName;

    @Column(name = "service_fee")
    private Float serviceFee;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "nights")
    private Integer nights;

    @Column(name = "price_per_nights")
    private Float pricePerNights;

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