package com.capstone.unwind.model.RoomDTO;

import com.capstone.unwind.entity.RoomAmenity;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link RoomAmenity}
 */
@Value
public class RoomAmenityDto implements Serializable {
    Integer id;
    String name;
    String type;
}