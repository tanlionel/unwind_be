package com.capstone.unwind.model.CustomerDTO;

import com.capstone.unwind.entity.Customer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Customer}
 */
@Value
public class UpdateProfileDto implements Serializable {
    String fullName;
    String avatar;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dob;
    String address;
    String gender;
    String phone;
}