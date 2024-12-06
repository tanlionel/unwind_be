package com.capstone.unwind.model.PostingDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder

public class PostingResponseTsStaffDTO {
    private Integer rentalPostingId;
    private Float priceValuation;
    private Integer timeShareId;
    private Integer roomInfoId;
    private Integer rentalPackageId;
    private String rentalPackageName;
    private String roomCode;
    private Integer resortId;
    private String resortImage;
    private String resortName;
    private Float pricePerNights;
    private Float totalPrice;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkinDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkoutDate;
    private String status;
    private boolean isActive;
    @JsonIgnore
    private Boolean isValid;
}
