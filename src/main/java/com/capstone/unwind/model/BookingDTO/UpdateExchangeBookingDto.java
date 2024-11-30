package com.capstone.unwind.model.BookingDTO;

import com.capstone.unwind.entity.ExchangeBooking;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExchangeBookingDto implements Serializable {
    String primaryGuestName;
    String primaryGuestPhone;
    String primaryGuestEmail;
    String primaryGuestCountry;
    String primaryGuestStreet;
    String primaryGuestCity;
    String primaryGuestState;
    String primaryGuestPostalCode;
}