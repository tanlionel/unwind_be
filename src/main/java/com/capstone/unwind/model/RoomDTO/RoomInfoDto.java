package com.capstone.unwind.model.RoomDTO;

import com.capstone.unwind.entity.RoomInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link RoomInfo}
 */
@Value
public class RoomInfoDto implements Serializable {
    Integer id;
    String roomInfoCode;
    String roomInfoName;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    Timestamp createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    Timestamp updatedAt;
    Boolean isActive;
    Integer resortId;
    String status;
    Integer unitTypeId;
}