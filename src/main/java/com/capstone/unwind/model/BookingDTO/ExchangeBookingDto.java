package com.capstone.unwind.model.BookingDTO;

import com.capstone.unwind.entity.ExchangeBooking;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeBookingDto implements Serializable {
    Integer id;
    String status;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkinDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkoutDate;
    String primaryGuestName;
    String primaryGuestPhone;
    String primaryGuestEmail;
    String primaryGuestCountry;
    String primaryGuestStreet;
    String primaryGuestCity;
    String primaryGuestState;
    String primaryGuestPostalCode;
    Boolean isActive;
    Boolean isFeedback;
    Boolean isPrimaryGuest;
    String renterFullLegalName;
    String renterLegalPhone;
    String renterLegalAvatar;
    Float serviceFee;
    Integer nights;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp updatedDate;
}