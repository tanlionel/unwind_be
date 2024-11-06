package com.capstone.unwind.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@IdClass(MergedBookingId.class)
@Table(name = "merged_bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MergedBooking {

    @Id
    @Column(name = "booking_id")
    private Long bookingId;

    @Id
    @Column(name = "source")
    private String source;

    @Column(name = "renter_id")
    private Integer renterId;

    @Column(name = "status")
    private String status;

    @Column(name = "nights")
    private Integer nights;

    @Column(name = "checkin_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkinDate;

    @Column(name = "checkout_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkoutDate;

    @Column(name = "primary_guest_name")
    private String primaryGuestName;

    @Column(name = "primary_guest_phone")
    private String primaryGuestPhone;

    @Column(name = "primary_guest_email")
    private String primaryGuestEmail;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "renter_full_legal_name")
    private String renterFullLegalName;

    @Column(name = "renter_legal_avatar")
    private String renterLegalAvatar;

    @Column(name = "renter_legal_phone")
    private String renterLegalPhone;

    @Column(name = "created_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdDate;

    @Column(name = "unit_type_title")
    private String unitTypeTitle;

    @Column(name = "resort_id")
    private Long resortId;

    @Column(name = "resort_name")
    private String resortName;

    @Column(name = "logo")
    private String logo;
}
