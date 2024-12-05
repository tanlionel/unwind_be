package com.capstone.unwind.model.DashboardDTO;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueCostByDateDto {
    private Long revenueByDate;
    private Long revenueByCosts;
}
