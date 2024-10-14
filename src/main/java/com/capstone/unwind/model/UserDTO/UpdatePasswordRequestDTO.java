package com.capstone.unwind.model.UserDTO;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequestDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
