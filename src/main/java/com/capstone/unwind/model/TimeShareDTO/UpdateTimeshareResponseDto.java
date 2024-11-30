package com.capstone.unwind.model.TimeShareDTO;

import com.capstone.unwind.entity.Timeshare;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTimeshareResponseDto implements Serializable {
    Integer id;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp updatedDate;
    Integer startYear;
    Integer endYear;
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/Bangkok")
    LocalDate startDate;
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/Bangkok")
    LocalDate endDate;
    Boolean isActive;
}