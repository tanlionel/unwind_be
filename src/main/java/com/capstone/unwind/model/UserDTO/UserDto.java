package com.capstone.unwind.model.UserDTO;

import com.capstone.unwind.entity.User;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Value
public class UserDto implements Serializable {
    Integer id;
    String userName;
    String email;
    Integer roleId;
    String roleRoleName;
    Boolean isActive;
}