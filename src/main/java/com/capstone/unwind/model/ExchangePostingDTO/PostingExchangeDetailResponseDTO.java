package com.capstone.unwind.model.ExchangePostingDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PostingExchangeDetailResponseDTO {
    private Integer exchangePostingId;
    private String description;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate expiredDate;
    private Integer ownerId;
    private String ownerName;
    private Integer timeShareId;
    private Integer roomInfoId;
    private String roomName;
    private Integer resortId;
    private String resortName;
    private String address;
    private Boolean isVerify;
    private Integer nights;
    private Integer exchangePackageId;
    private String exchangePackageName;
    private String exchangePackageDuration;
    private String exchangePackageDescription;
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
    private  List<String> imageUrls;
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
}