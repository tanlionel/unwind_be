package com.capstone.unwind.model.TimeShareStaffDTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class TimeShareStaffUpdateRequestDTO {

    private Integer resortId;
    private Boolean isActive;
}
