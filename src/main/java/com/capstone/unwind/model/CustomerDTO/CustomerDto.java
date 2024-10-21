package com.capstone.unwind.model.CustomerDTO;

import com.capstone.unwind.entity.Customer;
import com.capstone.unwind.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Customer}
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto implements Serializable {
    Integer id;
    String fullName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dob;
    String address;
    String gender;
    String phone;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate memberPurchaseDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate memberExpiryDate;
    Integer membershipId;
    String country;
    String street;
    String city;
    String state;
    String postalCode;
    String note;
    UserDto user;
    Boolean isActive;
    Boolean isMember;



    /**
     * DTO for {@link User}
     */
    @Value
    public static class UserDto implements Serializable {
        Integer id;
        String userName;
        String email;
        Boolean isActive;
    }
}