package com.capstone.unwind.controller;

import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.DashboardDTO.CustomerDashboardDto;
import com.capstone.unwind.model.TotalPackageDTO.TotalPackageDto;
import com.capstone.unwind.service.ServiceInterface.DashboardService;
import com.capstone.unwind.service.ServiceInterface.ExchangePostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
@CrossOrigin
public class DashboardController {

    @Autowired
    DashboardService dashboardService;


    @GetMapping("/system-staff/total-packages")
    public ResponseEntity<TotalPackageDto> getTotalPackage() {
        TotalPackageDto totalPackage = dashboardService.getTotalPackage();
        return ResponseEntity.ok(totalPackage);
    }
    @GetMapping("/system-staff/total-packages/date")
    public ResponseEntity<TotalPackageDto> getTotalPackageByDate(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.atTime(LocalTime.MAX));
        TotalPackageDto totalPackage = dashboardService.getTotalPackageByDate(startTimestamp, endTimestamp);
        return ResponseEntity.ok(totalPackage);
    }
    @GetMapping("/system-staff/total-customers")
    public ResponseEntity<Long> getTotalCustomers() {
        return ResponseEntity.ok(dashboardService.getTotalCustomers());
    }
    @GetMapping("/system-staff/total-resorts")
    public ResponseEntity<Long> getTotalResorts() {
        return ResponseEntity.ok(dashboardService.getTotalResorts());
    }

    @GetMapping("/timeshare-company/total-money")
    public ResponseEntity<Float> getTotalMoneys( ) throws OptionalNotFoundException {
        return ResponseEntity.ok(dashboardService.getAvailableMoney());
    }
    @GetMapping("/timeshare-company/total-resorts")
    public ResponseEntity<Long> getTotalResortsByTsId() throws OptionalNotFoundException {
        return ResponseEntity.ok(dashboardService.getTotalResortsByTsId());
    }
    @GetMapping("/timeshare-company/total-staffs")
    public ResponseEntity<Long> getTotalStaffsByTsId() throws OptionalNotFoundException {
        return ResponseEntity.ok(dashboardService.getTotalStaffByTsId());
    }
    @GetMapping("/system-staff/total-company")
    public ResponseEntity<Long> getTotalCompany() throws OptionalNotFoundException {
        return ResponseEntity.ok(dashboardService.getTotalCompany());
    }
    @GetMapping("/timeshare-company/total-money/month")
    public ResponseEntity<Map<String, Double>> getMonthlyMoneyReceived() throws OptionalNotFoundException {
        return ResponseEntity.ok(dashboardService.getMonthlyMoneyReceivedInLast12Months());
    }
    @GetMapping("/customer/dashboard")
    public ResponseEntity<CustomerDashboardDto> getCustomerDashboard() {
        CustomerDashboardDto customerDashboard = dashboardService.getCustomerDashboard();
        return ResponseEntity.ok(customerDashboard);
    }
}