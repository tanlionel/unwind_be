package com.capstone.unwind.model.TimeShareDTO;

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
public class ListTimeShareDTO {
    private Integer timeShareId;
    private String resortName;
    private String resortImage;
    private String roomCode;
    private Integer bathRoom;
    private Integer bedRooms;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;
}
