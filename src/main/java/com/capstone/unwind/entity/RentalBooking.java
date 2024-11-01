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
@Table(name = "rental_booking")
public class RentalBooking {
    @Id
    @Column(name = "rental_booking_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_posting_id")
    private RentalPosting rentalPosting;

    @Column(name = "status", length = 45)
    private String status;

    @Column(name = "checkin_date")
    private LocalDate checkinDate;

    @Column(name = "checkout_date")
    private LocalDate checkoutDate;

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

    @Column(name = "is_feedback")
    private Boolean isFeedback;

    @Column(name = "renter_full_legal_name", length = 45)
    private String renterFullLegalName;

    @Column(name = "service_fee", length = 45)
    private String serviceFee;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "total_nights")
    private Integer totalNights;

    @Column(name = "price_per_nights")
    private Integer pricePerNights;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id")
    private Customer renter;
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