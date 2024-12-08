package com.capstone.unwind.model.ResortDTO;

import com.capstone.unwind.entity.UnitType;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link UnitType}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
public class UnitTypeDto implements Serializable {
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
    List<UnitTypeAmenities> unitTypeAmenitiesList;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Getter
    @Setter
    @Builder
    public static class UnitTypeAmenities{
        String name;
        String type;
        Boolean isActive;
    }
}