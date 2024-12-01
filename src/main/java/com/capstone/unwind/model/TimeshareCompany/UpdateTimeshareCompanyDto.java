package com.capstone.unwind.model.TimeshareCompany;

import com.capstone.unwind.entity.TimeshareCompany;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link TimeshareCompany}
 */
@Value
public class UpdateTimeshareCompanyDto implements Serializable {
    String timeshareCompanyName;
    String logo;
    LocationDTO location;
    String description;
    String contact;
    List<String> imageUrls;

    @Data
    @Getter
    @Setter
    @Builder
    public static class LocationDTO {
        private String name;
        private String displayName;
        private String latitude;
        private String longitude;
        private String country;
        private String placeId;
    }
}