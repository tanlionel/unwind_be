package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangePosting;
import com.capstone.unwind.entity.ExchangeRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * DTO for {@link ExchangeRequest}
 */
@Value
public class ExchangeRequestBasicDto implements Serializable {
    Integer id;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate endDate;
    String status;
    ExchangePostingDto exchangePosting;
    String note;
    @JsonFormat(pattern = "dd-MM-yyyy HH::mm:ss", timezone = "Asia/Bangkok")
    Timestamp createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH::mm:ss", timezone = "Asia/Bangkok")
    Timestamp updatedDate;
    Boolean isActive;

    /**
     * DTO for {@link ExchangePosting}
     */
    @Value
    public static class ExchangePostingDto implements Serializable {
        Integer id;
        String description;
        Integer nights;
        Integer roomInfoResortId;
        String roomInfoResortResortName;
        String roomInfoResortLogo;
        Integer roomInfoUnitTypeId;
        String roomInfoUnitTypeTitle;
        String roomInfoUnitTypePhotos;
    }
}