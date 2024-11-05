package com.capstone.unwind.model.BookingDTO;

import com.capstone.unwind.entity.RentalBooking;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link RentalBooking}
 */
@Value
public class RentalBookingRequestDto implements Serializable {
    String primaryGuestName;
    String primaryGuestPhone;
    String primaryGuestEmail;
}