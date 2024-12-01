package com.capstone.unwind.model.ResortDTO;

import com.capstone.unwind.entity.Resort;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResortDto implements Serializable {
    Integer id;
    String resortName;
    String resortLocationName;
    String resortLocationDisplayName;
    String logo;
    Float minPrice;
    Float maxPrice;
    String status;
    Integer timeshareCompanyId;
    Boolean isActive;
    Float averageRating;
    Long totalRating;
}