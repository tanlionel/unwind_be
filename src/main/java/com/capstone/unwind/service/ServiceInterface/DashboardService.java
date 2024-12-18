package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.DashboardDTO.AdminDashboardBalanceDto;
import com.capstone.unwind.model.DashboardDTO.AdminDashboardDto;
import com.capstone.unwind.model.DashboardDTO.CustomerDashboardDto;
import com.capstone.unwind.model.DashboardDTO.CustomerMoneyDashboardDto;
import com.capstone.unwind.model.TotalPackageDTO.PackageDashboardDto;
import com.capstone.unwind.model.TotalPackageDTO.TotalPackageDto;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DashboardService {
    Long getTotalCustomers();
    Long getTotalResorts();
    TotalPackageDto getTotalPackage();
    Long getTotalResortsByTsId()throws OptionalNotFoundException;
    Long getTotalStaffByTsId( )throws OptionalNotFoundException;
    Float getAvailableMoney( )throws OptionalNotFoundException;
    Long getTotalCompany() throws OptionalNotFoundException;
    Map<String, Double> getMonthlyMoneyReceivedInLast12Months();
    CustomerDashboardDto getCustomerDashboard();
    List<PackageDashboardDto> getTotalPackageByDate(Timestamp startDate, Timestamp endDate) throws ErrMessageException;

    CustomerMoneyDashboardDto getCustomerMoneyDashboard(Timestamp startDate, Timestamp endDate) throws ErrMessageException;

    AdminDashboardDto getAdminDashboard();

    AdminDashboardBalanceDto getAdminDashboardRevuenue() throws ErrMessageException;
}
