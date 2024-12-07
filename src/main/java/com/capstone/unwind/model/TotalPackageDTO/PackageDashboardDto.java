package com.capstone.unwind.model.TotalPackageDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.Map;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageDashboardDto {
    @JsonFormat(pattern = "dd-MM-yyyy")
    Date date;
    Long totalRentalPackage;
    Long totalExchangePackage;
    Long totalMemberShip;
}
