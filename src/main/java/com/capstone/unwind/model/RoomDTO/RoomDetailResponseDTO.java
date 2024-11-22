package com.capstone.unwind.model.RoomDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Getter
@Setter
@Builder
public class RoomDetailResponseDTO implements Serializable {
    private Integer roomId;
    private String roomInfoCode;
    private Boolean isActive;
    private Integer resortId;
    private String roomName;
    private String status;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    private Timestamp createdAt;
    private List<roomAmenity> roomAmenities;
    @Data
    @Getter
    @Setter
    @Builder
    public static class roomAmenity{
        String name;
        String type;
        Boolean isActive;
    }

}
