package com.capstone.unwind.model.CustomerDTO;

import com.capstone.unwind.entity.Customer;
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
public class CustomerInitDto implements Serializable {
    Integer id;
    String fullName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate memberExpiryDate;
    String membershipName;
    Integer userId;
    String userUserName;
    String userRoleRoleName;
    Boolean isActive;
    Integer walletId;
    Float walletAvailableMoney;
    Boolean isMember;
}