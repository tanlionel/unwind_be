package com.capstone.unwind.model.TimeShareDTO;

import com.capstone.unwind.entity.Customer;
import com.capstone.unwind.entity.RoomInfo;
import jakarta.persistence.*;
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
public class TimeShareRequestDTO {

    private String status;

    private Integer startYear;

    private Integer endYear;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer roomInfoId;

}
