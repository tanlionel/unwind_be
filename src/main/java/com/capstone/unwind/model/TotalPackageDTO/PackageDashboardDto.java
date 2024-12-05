package com.capstone.unwind.model.TotalPackageDTO;

import com.capstone.unwind.model.DashboardDTO.RevenueCostByDateDto;
import lombok.*;

import java.util.Map;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageDashboardDto {
    private Map<String, TotalPackageDto> packageByDateMap;
}
