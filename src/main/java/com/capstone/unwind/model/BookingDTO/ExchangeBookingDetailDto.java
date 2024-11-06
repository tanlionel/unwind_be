package com.capstone.unwind.model.BookingDTO;

import com.capstone.unwind.entity.ExchangeBooking;
import com.capstone.unwind.entity.RoomInfo;
import com.capstone.unwind.entity.UnitType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * DTO for {@link ExchangeBooking}
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeBookingDetailDto implements Serializable {
    Integer id;
    RoomInfoDto roomInfo;
    String status;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkinDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkoutDate;
    String type;
    String primaryGuestName;
    String primaryGuestPhone;
    String primaryGuestEmail;
    Boolean isActive;
    Boolean isFeedback;
    String renterFullLegalName;
    String renterLegalPhone;
    String renterLegalAvatar;
    Float serviceFee;
    Integer nights;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    Timestamp createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    Timestamp updatedDate;
    String source;

    /**
     * DTO for {@link RoomInfo}
     */
    @Value
    public static class RoomInfoDto implements Serializable {
        String roomInfoCode;
        String roomInfoName;
        UnitTypeDto unitType;

        /**
         * DTO for {@link UnitType}
         */
        @Value
        public static class UnitTypeDto implements Serializable {
            Integer id;
            String title;
            Float price;
            String description;
            String photos;
            Integer resortId;
            String resortName;
            String resortLogo;
            String resortDescription;
        }
    }
}