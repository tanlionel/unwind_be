package com.capstone.unwind.model.ExchangePostingDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
public class PostingExchangeResponseDTO {
    private Integer exchangePostingId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate expired;
    private Integer ownerId;
    private String ownerName;
    private Integer timeShareId;
    private Integer roomInfoId;
    private String roomName;
    private Integer resortId;
    private String resortName;
    String resortLocationName;
    String resortLocationDisplayName;
    private Boolean isVerify;
    private Integer nights;
    private Integer exchangePackageId;
    private String exchangePackageName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkinDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkoutDate;
    private String status;
    private boolean isActive;
    private unitTypeDTO unitTypeDTO;

    @Data
    @Getter
    @Setter
    @Builder
    public static class unitTypeDTO {
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
}
