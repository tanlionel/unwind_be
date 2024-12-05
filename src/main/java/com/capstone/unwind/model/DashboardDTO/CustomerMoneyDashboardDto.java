package com.capstone.unwind.model.DashboardDTO;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMoneyDashboardDto {
    private Long totalRevenue;
    private Long totalCosts;
    private Map<String, RevenueCostByDateDto> revenueCostByDateMap;
}
