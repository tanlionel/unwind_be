package com.capstone.unwind.model.ExchangePostingDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
public class ExchangePostingResponseTsStaffDTO {
    private Integer exchangePostingId;
    private Integer timeShareId;
    private Integer roomInfoId;
    private Integer exchangePackageId;
    private String exchangePackageName;
    private String roomCode;
    private Integer resortId;
    private String resortName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkinDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkoutDate;
    private String status;
    private boolean isActive;
}
