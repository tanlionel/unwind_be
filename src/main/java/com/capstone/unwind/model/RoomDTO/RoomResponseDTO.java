package com.capstone.unwind.model.RoomDTO;

import com.capstone.unwind.entity.RoomAmenity;
import com.capstone.unwind.entity.RoomInfo;
import com.capstone.unwind.entity.UnitType;
import com.capstone.unwind.model.ResortDTO.ResortRequestDTO;
import com.capstone.unwind.model.ResortDTO.UnitTypeDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
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
public class RoomResponseDTO implements Serializable {
    private Integer roomId;
    private String roomInfoCode;
    private Boolean isActive;
    private Integer resortId;
    private String roomName;
    private String status;
    private unitType unitType;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
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
    @Data
    @Getter
    @Setter
    @Builder
    public static class unitType{
        Integer id;
        String title;
        String area;
        Integer bathrooms;
        Integer bedrooms;
        Integer bedsFull;
        Integer bedsKing;
        Integer bedsSofa;
        Integer bedsMurphy;
        Integer bedsQueen;
        Integer bedsTwin;
        String buildingsOption;
        Float price;
        String description;
        String kitchen;
        String photos;
        Integer sleeps;
        String view;
        Boolean isActive;
    }
}
