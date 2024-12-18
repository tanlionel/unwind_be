package com.capstone.unwind.model.DashboardDTO;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardBalanceDto {
    Float membershipRevuenue;
    Float rentalPostingRevuenue;
    Float exchangePostingRevuenue;
    Float totalRevuenue;
}
