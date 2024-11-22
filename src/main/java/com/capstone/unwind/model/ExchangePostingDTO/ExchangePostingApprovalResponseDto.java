package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangePosting;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * DTO for {@link ExchangePosting}
 */
@Value
public class ExchangePostingApprovalResponseDto implements Serializable {
    Integer id;
    Integer nights;
    Boolean isVerify;
    Boolean isExchange;
    String status;
    Integer exchangePackageId;
    String exchangePackagePackageName;
    Integer ownerId;
    String ownerPostalCode;
    Boolean isActive;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkinDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkoutDate;
    String note;
    Integer timeshareId;
    Integer roomInfoId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate expired;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp updatedDate;
}