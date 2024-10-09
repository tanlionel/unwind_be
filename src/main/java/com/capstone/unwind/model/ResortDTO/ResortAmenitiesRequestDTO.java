package com.capstone.unwind.model.ResortDTO;

import lombok.Value;

import java.util.List;


public record ResortAmenitiesRequestDTO(List<ResortAmenity> resortAmenityList, Integer resortId) {

    public  record ResortAmenity(String name, String type) {
    }
}
