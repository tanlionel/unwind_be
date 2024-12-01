package com.capstone.unwind.model.ResortDTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
public class ResortRequestDTO {
    String resortName;
    String logo;
    Float minPrice;
    Float maxPrice;
    String status;
    LocationDTO location;
    Integer timeshareCompanyId;
    String description;
    List<ResortAmenity> resortAmenityList;
    List<String> ImageUrls;

    @Data
    @Getter
    @Setter
    @Builder
    public static class ResortAmenity{
        String name;
        String type;
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
