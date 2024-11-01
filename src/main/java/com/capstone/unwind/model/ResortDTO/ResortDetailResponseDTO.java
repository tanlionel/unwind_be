package com.capstone.unwind.model.ResortDTO;

import com.capstone.unwind.model.FeedbackDTO.FeedbackResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
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
    String description;
    List<ResortAmenity> resortAmenityList;
    Boolean isActive;
    List<UnitTypeDto> unitTypeDtoList;
    List<Feedback> feedbackList;

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
    public static class Feedback{
        Float ratingPoint;
        String comment;
        CustomerDto user;
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        Timestamp createdDate;
        Boolean isActive;
    }
    @Data
    @Getter
    @Setter
    @Builder
    public static class CustomerDto  {
        String fullName;
        String avatar;
    }
}
