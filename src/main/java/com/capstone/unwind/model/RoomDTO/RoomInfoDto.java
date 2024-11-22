package com.capstone.unwind.model.RoomDTO;

import com.capstone.unwind.entity.RoomInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * DTO for {@link RoomInfo}
 */
@Value
public class RoomInfoDto implements Serializable {
    Integer id;
    String roomInfoCode;
    String roomInfoName;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp updatedAt;
    Boolean isActive;
    Integer resortId;
    String status;
    Integer unitTypeId;

}