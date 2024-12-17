package com.capstone.unwind.model.DashboardDTO;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardDto {
    Long totalUser;
    Long totalCustomer;
    Long totalTimeshareStaff;
    Long totalTimeshareCompany;
    Long totalSystemStaff;
    Long totalAdmin;
}
