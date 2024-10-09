package com.capstone.unwind.model.UserDTO;

import com.capstone.unwind.entity.User;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    Integer id;
    String email;
    String userName;
    Integer roleId;
    String roleRoleName;
    Boolean isActive;
}