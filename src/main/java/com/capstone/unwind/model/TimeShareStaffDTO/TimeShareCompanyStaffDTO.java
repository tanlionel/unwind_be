package com.capstone.unwind.model.TimeShareStaffDTO;

import com.capstone.unwind.entity.Resort;
import com.capstone.unwind.entity.TimeshareCompany;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class TimeShareCompanyStaffDTO {

    private Integer id;
    private String userName;
    private Integer timeshareCompanyId;

    private Integer resortId;

    private Boolean isActive;
}
