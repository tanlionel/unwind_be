package com.capstone.unwind.model.ResortDTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitTypeResponseDTO {
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
    Integer resortId;
    Integer sleeps;
    String view;
    Boolean isActive;
    List<UnitTypeAmenitiesDTO> unitTypeAmenitiesDTOS;
    @Data
    @Getter
    @Setter
    @Builder
    public static class UnitTypeAmenitiesDTO {
        private String name;
        private String type;
    }

}
