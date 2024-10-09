package com.capstone.unwind.model.ResortDTO;

import com.capstone.unwind.entity.Resort;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Resort}
 */
@Value
public class ResortDto implements Serializable {
    Integer id;
    String resortName;
    String logo;
    Float minPrice;
    Float maxPrice;
    String status;
    String address;
    Integer timeshareCompanyId;
    Boolean isActive;
}