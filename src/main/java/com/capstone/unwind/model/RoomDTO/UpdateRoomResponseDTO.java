package com.capstone.unwind.model.RoomDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoomResponseDTO {
    private RoomInfoDto roomInfo;
    private List<RoomAmenityDto> roomAmenities;
}
