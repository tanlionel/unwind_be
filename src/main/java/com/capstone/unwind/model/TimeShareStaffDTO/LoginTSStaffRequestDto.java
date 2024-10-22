package com.capstone.unwind.model.TimeShareStaffDTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class LoginTSStaffRequestDto {
    private String username;
    private String password;
    private Integer tsCompanyId;
}
