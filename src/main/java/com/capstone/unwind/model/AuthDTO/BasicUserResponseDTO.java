package com.capstone.unwind.model.AuthDTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
public class BasicUserResponseDTO {
    private String email;
    private String username;
}
