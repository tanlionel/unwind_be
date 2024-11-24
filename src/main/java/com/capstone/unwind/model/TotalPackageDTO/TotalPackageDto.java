package com.capstone.unwind.model.TotalPackageDTO;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalPackageDto {
    Long totalRentalPackage;
    Long totalExchangePackage;
    Long totalMemberShip;
}
