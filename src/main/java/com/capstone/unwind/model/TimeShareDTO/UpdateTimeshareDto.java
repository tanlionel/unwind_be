package com.capstone.unwind.model.TimeShareDTO;

import com.capstone.unwind.entity.Timeshare;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTimeshareDto implements Serializable {
    Integer startYear;
    Integer endYear;
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/Bangkok")
    LocalDate startDate;
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/Bangkok")
    LocalDate endDate;
    Integer roomInfoId;
}