package com.capstone.unwind.model.TimeShareDTO;

import com.capstone.unwind.entity.Customer;
import com.capstone.unwind.entity.RoomInfo;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
public class TimeShareResponseDTO {
    private Integer timeShareId;
    private String status;

    private Integer startYear;

    private Integer endYear;

    private LocalDate startDate;

    private LocalDate endDate;

    private String owner;
    private Timestamp createdAt;

    private Boolean isActive;

    private RoomInfo roomInfo;
}
