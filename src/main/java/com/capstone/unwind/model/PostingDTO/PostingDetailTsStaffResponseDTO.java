package com.capstone.unwind.model.PostingDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@Builder

public class PostingDetailTsStaffResponseDTO {
    private Integer rentalPostingId;
    private String description;
    private Float priceValuation;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    private Timestamp createdDate;
    private Integer timeShareId;
    private Integer ownerId;
    private String ownerName;
    private Integer roomInfoId;
    private String roomCode;
    private String roomName;
    private Integer resortId;
    private String resortName;
    private String resortImage;
    private String resortDescription;
    private LocationDTO location;
    private Boolean isVerify;
    private Integer nights;
    private Float pricePerNights;
    private Float totalPrice;
    private Integer cancelTypeId;
    private String cancelType;
    private Integer rentalPackageId;
    private String rentalPackageName;
    private String rentalPackageDescription;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkinDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkoutDate;
    private String status;
    private boolean isActive;
    private unitType unitType;
    private List<ResortAmenityDTO> resortAmenities;
    private List<RoomAmenityDTO> roomAmenities;
    private List<UnitTypeAmenityDTO> unitTypeAmenities;
    private List<String> imageUrls;
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
        public static class UnitTypeAmenityDTO {
            private Integer id;
            private String name;
            private String type;
        }

    @Data
    @Getter
    @Setter
    @Builder
    public static class ResortAmenityDTO {
        private Integer id;
        private String name;
        private String type;
    }

    @Data
    @Getter
    @Setter
    @Builder
    public static class RoomAmenityDTO {
        private Integer id;
        private String name;
        private String type;
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