package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.model.TotalPackageDTO.TotalPackageDto;

public interface DashboardService {
    Long getTotalCustomers();
    Long getTotalResorts();
    TotalPackageDto getTotalPackage();
}
