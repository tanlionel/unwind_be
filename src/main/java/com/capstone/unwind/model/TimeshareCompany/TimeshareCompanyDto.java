package com.capstone.unwind.model.TimeshareCompany;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.capstone.unwind.entity.TimeshareCompany}
 */
@Value
public class TimeshareCompanyDto implements Serializable {
    Integer id;
    String timeshareCompanyName;
    String logo;
    String address;
    String description;
    Integer ownerId;
    String contact;
    Boolean isActive;
}