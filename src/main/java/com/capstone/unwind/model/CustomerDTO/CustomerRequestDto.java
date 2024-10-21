package com.capstone.unwind.model.CustomerDTO;

import com.capstone.unwind.entity.User;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

@Value
public class CustomerRequestDto {
    String fullName;
    LocalDate dob;
    String address;
    String gender;
    String phone;
}
