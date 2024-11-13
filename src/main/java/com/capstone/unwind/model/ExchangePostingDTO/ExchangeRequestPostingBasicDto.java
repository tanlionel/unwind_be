package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangeRequest;
import com.capstone.unwind.entity.RoomInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * DTO for {@link ExchangeRequest}
 */
@Value
public class ExchangeRequestPostingBasicDto implements Serializable {
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
    String note;
    @JsonFormat(pattern = "dd-MM-yyyy HH::mm:ss")
    Timestamp createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH::mm:ss")
    Timestamp updatedDate;
    Boolean isActive;

    /**
     * DTO for {@link RoomInfo}
     */
    @Value
    public static class RoomInfoDto implements Serializable {
        Integer id;
        String roomInfoCode;
        @JsonFormat(pattern = "dd-MM-yyyy HH::mm:ss")
        Timestamp createdAt;
        @JsonFormat(pattern = "dd-MM-yyyy HH::mm:ss")
        Timestamp updatedAt;
        Boolean isActive;
        Integer resortId;
        String resortResortName;
        String resortLogo;
        String resortAddress;
        String resortDescription;
        String status;
        Integer unitTypeId;
        String unitTypeTitle;
        String unitTypePhotos;
    }
}