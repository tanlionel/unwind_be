package com.capstone.unwind.model.AuthDTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseObjectDTO {
    private String accessToken;
    private String refreshToken;
}
