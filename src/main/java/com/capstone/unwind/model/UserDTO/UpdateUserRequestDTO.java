package com.capstone.unwind.model.UserDTO;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDTO {
    Integer roleId;
    Boolean isActive;
}
