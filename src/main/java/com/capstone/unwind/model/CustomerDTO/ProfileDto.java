package com.capstone.unwind.model.CustomerDTO;

import com.capstone.unwind.entity.Customer;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
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
public class ProfileDto implements Serializable {
    Integer id;
    String fullName;
    String avatar;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dob;
    String address;
    String gender;
    String phone;
    Integer membershipId;
    String membershipName;
    Integer userId;
    String userUserName;
    String userEmail;
    Boolean isActive;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate memberPurchaseDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate memberExpiryDate;
    Boolean isMember;
}