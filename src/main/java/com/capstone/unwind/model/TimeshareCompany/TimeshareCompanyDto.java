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
    String address;
    String description;
    Integer ownerId;
    String contact;
    Boolean isActive;
    List<String> imageUrls ;
}