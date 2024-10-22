package com.capstone.unwind.model.AuthDTO;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class LoginResponseStaffDto {
    private String accessToken;
    private String refreshToken;
    private Integer resortId;
    private Integer tsCompanyId;
}

