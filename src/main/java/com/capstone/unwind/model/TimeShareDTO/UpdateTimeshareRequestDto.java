package com.capstone.unwind.model.TimeShareDTO;

import com.capstone.unwind.entity.RoomAmenity;
import com.capstone.unwind.entity.Timeshare;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTimeshareRequestDto implements Serializable {
    List<RoomAmenityDto> roomInfoAmenities;

    @Data
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomAmenityDto implements Serializable {
        String name;
        String type;
    }
}