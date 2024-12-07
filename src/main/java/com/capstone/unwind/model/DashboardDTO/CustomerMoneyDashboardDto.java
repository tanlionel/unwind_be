package com.capstone.unwind.model.DashboardDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMoneyDashboardDto {
    private Double totalRevenue;
    private Double totalCosts;
    List<RevenueCostByDateDto> revenueCostByDateDtos;

    @Data
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueCostByDateDto {
        @JsonFormat(pattern = "dd-MM-yyyy")
        private Date date;
        private Double revenueByDate;
        private Double revenueByCosts;
    }

}
