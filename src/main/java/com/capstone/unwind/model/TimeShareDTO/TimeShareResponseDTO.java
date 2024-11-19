package com.capstone.unwind.model.TimeShareDTO;

import com.capstone.unwind.entity.Customer;
import com.capstone.unwind.entity.RoomInfo;
import com.capstone.unwind.model.RoomDTO.RoomAmenityDto;
import com.capstone.unwind.model.RoomDTO.RoomInfoDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@Builder
public class TimeShareResponseDTO {
    private Integer timeShareId;
    private String status;
    private Integer startYear;
    private Integer endYear;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;
    private String owner;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp createdAt;
    private Boolean isActive;
    private RoomInfoDto roomInfo;
    private List<RoomAmenityDto> roomAmenities;
}
