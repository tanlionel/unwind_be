package com.capstone.unwind.model.TimeshareCompany;

import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TimeshareCompanyDto implements Serializable {
    Integer id;
    String timeshareCompanyName;
    String logo;
    LocationDTO location;;
    String description;
    Integer ownerId;
    String contact;
    Boolean isActive;
    List<String> imageUrls ;

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