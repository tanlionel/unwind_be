package com.capstone.unwind.model.RoomDTO;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Data
@Getter
@Setter
@Builder
public class RoomRequestDTO {
    private String roomInfoCode;
    private Boolean isActive;
    private Integer resortId;
    private String status;
    private Integer unitTypeId;
    private String roomName;
    private List<roomAmenity> roomAmenities;
    @Data
    @Getter
    @Setter
    @Builder
    public static class roomAmenity{
        String name;
        String type;
    }
}
