package com.capstone.unwind.model.TimeShareDTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
public class ListTimeShareDTO {
    private Integer timeShareId;
    private String resortName;
    private String roomName;
    private Integer bathRoom;
    private Integer bedRooms;
    private LocalDate startDate;
    private LocalDate endDate;
}
