package com.capstone.unwind.model.AuthDTO;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    private String email;
    private String username;
    private String password;
    private Integer roleId;
}
