package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.TimeshareCompany;
import com.capstone.unwind.entity.User;
import com.capstone.unwind.entity.Wallet;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.TotalPackageDTO.TotalPackageDto;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.DashboardService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImplement implements DashboardService {
    @Autowired
    private final RentalPostingRepository rentalPostingRepository;
    @Autowired
    private final ExchangePostingRepository exchangePostingRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ResortRepository resortRepository;
    @Autowired
    private final TimeshareCompanyStaffRepository timeshareCompanyStaffRepository;
    @Autowired
    private final WalletRepository walletRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final TimeshareCompanyRepository timeshareCompanyRepository;


    @Override
    public Long getTotalCustomers() {
        return userRepository.getTotalCustomers();
    }

    @Override
    public Long getTotalResorts() {
        return resortRepository.getTotalResorts();
    }
    @Override
    public Long getTotalResortsByTsId() throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(user.getId());
        if (timeshareCompany == null) throw new OptionalNotFoundException("Not init timeshare company yet");
        if (timeshareCompany.getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        return resortRepository.getTotalResorts(timeshareCompany.getId());
    }
    @Override
    public Long getTotalStaffByTsId() throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(user.getId());
        if (timeshareCompany == null) throw new OptionalNotFoundException("Not init timeshare company yet");
        if (timeshareCompany.getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        return timeshareCompanyStaffRepository.getTotalStaffs(timeshareCompany.getId());
    }
    @Override
    public Float getAvailableMoney() throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(user.getId());
        if (timeshareCompany == null) throw new OptionalNotFoundException("Not init timeshare company yet");
        if (timeshareCompany.getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        Wallet wallet = walletRepository.findWalletByTimeshareCompany_Id(timeshareCompany.getId());
        return wallet.getAvailableMoney();
    }
    @Override
    public TotalPackageDto getTotalPackage() {
        Long rentalPackage1 = rentalPostingRepository.getRentalPackage1();
        Long rentalPackage2 = rentalPostingRepository.getRentalPackage2();
        Long rentalPackage3 = rentalPostingRepository.getRentalPackage3();
        Long rentalPackage4 = rentalPostingRepository.getRentalPackage4();

        // Tính tổng các gói ExchangePackage
        Long exchangePackage1 = exchangePostingRepository.getExchangePackage1();
        Long exchangePackage2 = exchangePostingRepository.getExchangePackage2();

        return TotalPackageDto.builder()
                .rentalPackage1(rentalPackage1 != null ? rentalPackage1 : 0L)
                .rentalPackage2(rentalPackage2 != null ? rentalPackage2 : 0L)
                .rentalPackage3(rentalPackage3 != null ? rentalPackage3 : 0L)
                .rentalPackage4(rentalPackage4 != null ? rentalPackage4 : 0L)
                .exchangePackage1(exchangePackage1 != null ? exchangePackage1 : 0L)
                .exchangePackage2(exchangePackage2 != null ? exchangePackage2 : 0L)
                .build();
    }
}
