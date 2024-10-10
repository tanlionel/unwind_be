package com.capstone.unwind.model.ResortDTO;

import com.capstone.unwind.entity.UnitType;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link UnitType}
 */
@Value
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
}