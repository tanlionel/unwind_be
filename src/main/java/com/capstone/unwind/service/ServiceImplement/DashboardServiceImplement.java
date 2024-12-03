package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.Customer;
import com.capstone.unwind.entity.TimeshareCompany;
import com.capstone.unwind.entity.User;
import com.capstone.unwind.entity.Wallet;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.DashboardDTO.CustomerDashboardDto;
import com.capstone.unwind.model.TotalPackageDTO.TotalPackageDto;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.DashboardService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    private final CustomerRepository customerRepository;
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
    @Autowired
    private final WalletTransactionRepository walletTransactionRepository;
    @Autowired
    private final ExchangeRequestRepository exchangeRequestRepository;
    @Autowired
    private final RentalBookingRepository rentalBookingRepository;
    @Autowired
    private final ExchangeBookingRepository exchangeBookingRepository;


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
    public Long getTotalCompany()  {

        return timeshareCompanyRepository.getTotalCompany();
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
        Long rentalPackage = rentalPostingRepository.getRentalPackage();
        Long exchangePackage = exchangePostingRepository.getExchangePackage();
        Long membershipPackage = walletTransactionRepository.getTotalMEMBERSGIP();
        return TotalPackageDto.builder()
                .totalRentalPackage(rentalPackage != null ? rentalPackage : 0L)
                .totalExchangePackage(exchangePackage != null ? exchangePackage : 0L)
                .totalMemberShip(membershipPackage != null ? membershipPackage : 0L)
                .build();
    }
    @Override
    public TotalPackageDto getTotalPackageByDate(Timestamp startDate, Timestamp endDate) {
        Long rentalPackage = rentalPostingRepository.countRentalPackageByDateRange(startDate, endDate);
        Long exchangePackage = exchangePostingRepository.countExchangePackageByDateRange(startDate, endDate);
        Long membershipPackage = walletTransactionRepository.countMembershipPackageByDateRange(startDate, endDate);
        return TotalPackageDto.builder()
                .totalRentalPackage(rentalPackage != null ? rentalPackage : 0L)
                .totalExchangePackage(exchangePackage != null ? exchangePackage : 0L)
                .totalMemberShip(membershipPackage != null ? membershipPackage : 0L)
                .build();
    }
    @Override
    public CustomerDashboardDto getCustomerDashboard() {
        User user = userService.getLoginUser();
        Customer customer = customerRepository.findByUserId(user.getId());
        Long rentalPosting = rentalPostingRepository.getRentalPostingByUserId(customer.getId());
        Long exchangePosting = exchangePostingRepository.getExchangePostingByUserId(customer.getId());
        Long totalPosting = rentalPosting + exchangePosting;
        Long totalRentalRenter = rentalPostingRepository.getRentalRenterByUserId(customer.getId());
        Long totalExchangeRenter = exchangePostingRepository.getExchangeRenterByUserId(customer.getId());
        Long totalExchangerRequest = exchangeRequestRepository.getExchangeRequestByUserId(customer.getId());
        Long rentalBooking = rentalBookingRepository.getRentalBookingByUserId(customer.getId());
        Long exchangeBooking = exchangeBookingRepository.getExchangeBookingByUserId(customer.getId());
        Long totalBooking = rentalBooking + exchangeBooking;
        return CustomerDashboardDto.builder()
                .totalPosting(totalPosting != null ? totalPosting : 0L)
                .totalRentalRenter(totalRentalRenter != null ? totalRentalRenter : 0L)
                .totalExchangerRenter(totalExchangeRenter != null ? totalExchangeRenter : 0L)
                .totalRequest(totalExchangerRequest != null ? totalExchangerRequest : 0L)
                .totalBooking(totalBooking != null ? totalBooking : 0L)
                .build();
    }
    @Override
    public Map<String, Double> getMonthlyMoneyReceivedInLast12Months() {
        User user = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(user.getId());

        LocalDateTime startDate = LocalDate.now().minusMonths(12).atStartOfDay(ZoneId.systemDefault()).toLocalDateTime();

        List<Object[]> results = walletTransactionRepository.findMonthlyMoneyReceived(timeshareCompany.getId(), startDate);

        Map<String, Double> monthlyTotals = new LinkedHashMap<>();
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Integer year = (Integer) result[1];
            Double totalMoney = (Double) result[2];
            String key = String.format("%02d-%d", month, year);
            monthlyTotals.put(key, totalMoney);
        }
        return monthlyTotals;
    }
}
