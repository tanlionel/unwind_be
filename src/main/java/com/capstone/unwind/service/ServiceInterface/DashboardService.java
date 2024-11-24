package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.TotalPackageDTO.TotalPackageDto;

public interface DashboardService {
    Long getTotalCustomers();
    Long getTotalResorts();
    TotalPackageDto getTotalPackage();
    Long getTotalResortsByTsId()throws OptionalNotFoundException;
    Long getTotalStaffByTsId( )throws OptionalNotFoundException;
    Float getAvailableMoney( )throws OptionalNotFoundException;;
}
