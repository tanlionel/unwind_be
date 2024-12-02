package com.capstone.unwind.model.TimeShareDTO;

import com.capstone.unwind.model.RoomDTO.RoomAmenityDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@Builder
public class TimeShareDetailDTO {
    private Integer timeShareId;
    private String resortName;
    private String roomCode;
    private String roomName;
    private Integer roomId;
    private Integer startYear;
    private Integer endYear;
    private LocationDTO location;
    private String resortImage;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;
    private Integer resortId;
    private unitType unitType;
    private List<RoomAmenityDto> roomAmenities;
    @Data
    @Getter
    @Setter
    @Builder
    public static class unitType {
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
        String description;
        String kitchen;
        String photos;
        Integer sleeps;
        String view;
    }
    @Data
    @Getter
    @Setter
    @Builder
    public static class LocationDTO {
        String name;
        String displayName;
        String latitude;
        String longitude;
        String country;
        String placeId;
    }
}