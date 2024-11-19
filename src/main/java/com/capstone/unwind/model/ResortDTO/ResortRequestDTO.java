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
    String address;
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
        boolean isFree;
        String type;
    }

}
