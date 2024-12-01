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
public class RentalBookingDetailDto implements Serializable {
    Integer id;
    RentalPostingDto rentalPosting;
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

    /**
     * DTO for {@link RentalPosting}
     */
    @Value
    public static class RentalPostingDto implements Serializable {
        Integer id;
        String description;
        Boolean isVerify;
        Boolean isBookable;
        RoomInfoDto roomInfo;
        CancellationPolicyDto cancellationType;
        Integer rentalPackageId;
        String rentalPackageRentalPackageName;
        String rentalPackageType;
        Float rentalPackagePrice;
        Timestamp createdDate;
        Timestamp updatedDate;

        /**
         * DTO for {@link RoomInfo}
         */
        @Value
        public static class RoomInfoDto implements Serializable {
            Integer id;
            String roomInfoCode;
            String roomInfoName;
            Boolean isActive;
            String status;
            UnitTypeDto unitType;

            /**
             * DTO for {@link UnitType}
             */
            @Value
            public static class UnitTypeDto implements Serializable {
                Integer id;
                String title;
                String area;
                Integer bathrooms;
                Integer bedrooms;
                Integer bedsFull;
                Integer bedsKing;
                Integer bedsSofa;
                Integer bedsMurphy;
                Integer bedsQueen;
                Integer bedsTwin;
                String buildingsOption;
                Float price;
                String description;
                String kitchen;
                String photos;
                Integer resortId;
                String resortResortName;
                String resortLogo;
                String resortLocationName;
                String resortLocationDisplayName;
                String resortDescription;
                Integer sleeps;
                String view;
                Boolean isActive;
            }
        }

        /**
         * DTO for {@link CancellationPolicy}
         */
        @Value
        public static class CancellationPolicyDto implements Serializable {
            Integer id;
            String name;
            Integer refundRate;
            Integer durationBefore;
            String description;
            Boolean isActive;
        }
    }
}