package com.capstone.unwind.controller;

import com.capstone.unwind.model.TotalPackageDTO.TotalPackageDto;
import com.capstone.unwind.service.ServiceInterface.DashboardService;
import com.capstone.unwind.service.ServiceInterface.ExchangePostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/system-staff/")
@RequiredArgsConstructor
@CrossOrigin
public class DashboardController {

    @Autowired
    DashboardService dashboardService;


    @GetMapping("/total-packages")
    public ResponseEntity<TotalPackageDto> getTotalPackage() {
        TotalPackageDto totalPackage = dashboardService.getTotalPackage();
        return ResponseEntity.ok(totalPackage);
    }
    @GetMapping("/total-customers")
    public ResponseEntity<Long> getTotalCustomers() {
        return ResponseEntity.ok(dashboardService.getTotalCustomers());
    }
    @GetMapping("/total-resorts")
    public ResponseEntity<Long> getTotalResorts() {
        return ResponseEntity.ok(dashboardService.getTotalResorts());
    }
}