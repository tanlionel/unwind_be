package com.capstone.unwind.model.TimeShareDTO;

import com.capstone.unwind.entity.RoomInfo;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeShareDTO {
    private Integer timeShareId;
    private String status;

    private Integer startYear;

    private Integer endYear;

    private LocalDate startDate;

    private LocalDate endDate;

    private String owner;
    private Timestamp createdAt;

    private Boolean isActive;

    private Integer roomInfoId;
}
