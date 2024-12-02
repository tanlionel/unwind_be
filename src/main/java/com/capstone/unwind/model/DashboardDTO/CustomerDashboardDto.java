package com.capstone.unwind.model.DashboardDTO;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDashboardDto {
    Long totalPosting;
    Long totalRentalRenter;
    Long totalExchangerRenter;
    Long totalRequest;
    Long totalBooking;
}
