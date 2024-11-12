package com.capstone.unwind.model.TimeshareCompany;

import com.capstone.unwind.entity.TimeshareCompany;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link TimeshareCompany}
 */
@Value
public class UpdateTimeshareCompanyDto implements Serializable {
    String timeshareCompanyName;
    String logo;
    String address;
    String description;
    String contact;
    List<String> imageUrls;
}