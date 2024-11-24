package com.capstone.unwind.model.BookingDTO;

import com.capstone.unwind.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * DTO for {@link RentalBooking}
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalBookingDto implements Serializable {
    Integer id;
    String status;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkinDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkoutDate;
    String primaryGuestName;
    String primaryGuestPhone;
    String primaryGuestEmail;
    Boolean isActive;
    Boolean isFeedback;
    String renterFullLegalName;
    String renterLegalPhone;
    String renterLegalAvatar;
    Float serviceFee;
    Float totalPrice;
    Integer totalNights;
    Float pricePerNights;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp updatedDate;
    String source;


}