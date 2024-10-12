package com.capstone.unwind.model.TimeShareStaffDTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class TimeShareCompanyStaffRequestDTO {

    private String userName;

    private String password;

    private Integer resortId;

}
