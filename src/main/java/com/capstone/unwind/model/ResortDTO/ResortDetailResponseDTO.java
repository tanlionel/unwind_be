package com.capstone.unwind.model.ResortDTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
public class ResortDetailResponseDTO {
    Integer id;
    String resortName;
    String logo;
    Float minPrice;
    Float maxPrice;
    String status;
    String address;
    Integer timeshareCompanyId;
    List<ResortAmenity> resortAmenityList;
    Boolean isActive;
    List<UnitTypeDto> unitTypeDtoList;

    @Data
    @Getter
    @Setter
    @Builder
    public static class ResortAmenity{
        String name;
        String type;
    }

}
