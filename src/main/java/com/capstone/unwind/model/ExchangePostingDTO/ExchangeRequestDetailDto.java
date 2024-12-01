package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangePosting;
import com.capstone.unwind.entity.ExchangeRequest;
import com.capstone.unwind.entity.RoomInfo;
import com.capstone.unwind.entity.UnitType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * DTO for {@link ExchangeRequest}
 */
@Value
public class ExchangeRequestDetailDto implements Serializable {
    Integer id;
    RoomInfoDto roomInfo;
    Integer ownerId;
    String ownerFullName;
    String ownerAvatar;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate endDate;
    String status;
    ExchangePostingDto exchangePosting;
    String note;
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/Bangkok")
    Timestamp createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/Bangkok")
    Timestamp updatedDate;
    Boolean isActive;

    /**
     * DTO for {@link RoomInfo}
     */
    @Value
    public static class RoomInfoDto implements Serializable {
        Integer id;
        String roomInfoCode;
        @JsonFormat(pattern = "dd-MM-yyyy HH::mm:ss", timezone = "Asia/Bangkok")
        Timestamp createdAt;
        @JsonFormat(pattern = "dd-MM-yyyy HH::mm:ss", timezone = "Asia/Bangkok")
        Timestamp updatedAt;
        Boolean isActive;
        Integer resortId;
        String resortResortName;
        String resortLogo;
        String resortLocationName;
        String resortLocationDisplayName;
        String resortDescription;
        String status;
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
        }
    }

    /**
     * DTO for {@link ExchangePosting}
     */
    @Value
    public static class ExchangePostingDto implements Serializable {
        Integer id;
        String description;
        Integer nights;
        Boolean isVerify;
        Boolean isExchange;
        String status;
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate checkinDate;
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate checkoutDate;
        Integer roomInfoId;
        String roomInfoRoomInfoCode;
        Integer roomInfoResortId;
        String roomInfoResortResortName;
        String roomInfoResortLogo;
        Integer roomInfoUnitTypeId;
        String roomInfoUnitTypeTitle;
        Float roomInfoUnitTypePrice;
        String roomInfoUnitTypePhotos;
    }
}