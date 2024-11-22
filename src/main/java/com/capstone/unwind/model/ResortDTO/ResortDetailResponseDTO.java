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
    Float averageRating;
    Long totalRating;
    List<String> imageUrls;
    @Data
    @Getter
    @Setter
    @Builder
    public static class ResortAmenity{
        String name;
        boolean isFree;
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
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
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
