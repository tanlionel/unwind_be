package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * DTO for {@link RentalPosting}
 */
@Value
public class RentalPostingApprovalResponseDto implements Serializable {
    Integer id;
    Integer nights;
    Float pricePerNights;
    Boolean isVerify;
    Boolean isBookable;
    Integer timeshareId;
    Integer roomInfoId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkinDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate checkoutDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate expiredDate;
    String status;
    Float staffRefinementPrice;
    Boolean isActive;
    Integer rentalPackageId;
    String rentalPackageRentalPackageName;
    Integer ownerId;
    String note;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp updatedDate;
    Float priceValuation;
}