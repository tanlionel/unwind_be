package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.Customer;
import com.capstone.unwind.entity.TimeshareCompany;
import com.capstone.unwind.entity.User;
import com.capstone.unwind.entity.Wallet;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.DashboardDTO.AdminDashboardBalanceDto;
import com.capstone.unwind.model.DashboardDTO.AdminDashboardDto;
import com.capstone.unwind.model.DashboardDTO.CustomerDashboardDto;
import com.capstone.unwind.model.DashboardDTO.CustomerMoneyDashboardDto;
import com.capstone.unwind.model.TotalPackageDTO.PackageDashboardDto;
import com.capstone.unwind.model.TotalPackageDTO.TotalPackageDto;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.DashboardService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

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
    @Override
    public List<PackageDashboardDto> getTotalPackageByDate(Timestamp startDate, Timestamp endDate) throws ErrMessageException {

        if (startDate == null) {
            startDate = Timestamp.valueOf(LocalDateTime.now()); }
        if (endDate == null) {
            endDate = Timestamp.valueOf(LocalDateTime.now());}
        if (startDate.after(endDate)) {
            throw new ErrMessageException("The startDate must be less than or equal to the endDate.");
        }

        Long totalRentalPosting = 0L;
        Long totalExchangePosting = 0L;
        Long totalMembership = 0L;
        List<Timestamp> allDates = new ArrayList<>();
        Timestamp currentDate = startDate;

        while (!currentDate.toLocalDateTime().toLocalDate().isAfter(endDate.toLocalDateTime().toLocalDate())) {
            allDates.add(currentDate);
            currentDate = Timestamp.valueOf(currentDate.toLocalDateTime().plusDays(1));
        }

        Map<Date, Long> rentalMap = new LinkedHashMap<>();
        Map<Date, Long> exchangeMap = new LinkedHashMap<>();
        Map<Date, Long> membershipMap = new LinkedHashMap<>();

        for (Timestamp date : allDates) {
            LocalDate localDate = date.toLocalDateTime().toLocalDate();

            Long rentalPackageResults = rentalPostingRepository.countRentalPackageByDateRange(localDate);
            Long exchangePackageResults = exchangePostingRepository.countExchangePackageByDateRange(localDate);
            Long membershipPackageResults = walletTransactionRepository.countMembershipPackageByDateRange(localDate);

            rentalPackageResults = (rentalPackageResults == null) ? 0L : rentalPackageResults;
            exchangePackageResults = (exchangePackageResults == null) ? 0L : exchangePackageResults;
            membershipPackageResults = (membershipPackageResults == null) ? 0L : membershipPackageResults;

            totalRentalPosting += rentalPackageResults;
            totalExchangePosting += exchangePackageResults;
            totalMembership += membershipPackageResults;

            // Save results to maps
            rentalMap.put(new Date(date.getTime()), rentalPackageResults);
            exchangeMap.put(new Date(date.getTime()), exchangePackageResults);
            membershipMap.put(new Date(date.getTime()), membershipPackageResults);
        }

        List<PackageDashboardDto> packageByDateDtos = new ArrayList<>();
        rentalMap.forEach((date, rentalPackageResults) -> {
            Long exchangePackageResults = exchangeMap.getOrDefault(date, 0L);
            Long membershipPackageResults = membershipMap.getOrDefault(date, 0L);

            packageByDateDtos.add(PackageDashboardDto.builder()
                    .date(date)
                    .totalRentalPackage(rentalPackageResults)
                    .totalExchangePackage(exchangePackageResults)
                    .totalMemberShip(membershipPackageResults)
                    .build());
        });

        return packageByDateDtos;
    }



    private Timestamp convertToTimestamp(Object dateObj) {
        if (dateObj instanceof java.sql.Date) {
            return new Timestamp(((java.sql.Date) dateObj).getTime());
        } else if (dateObj instanceof java.sql.Timestamp) {
            return (Timestamp) dateObj;
        } else {
            throw new IllegalArgumentException("Unexpected date type: " + dateObj.getClass());
        }
    }

    @Override
    public CustomerMoneyDashboardDto getCustomerMoneyDashboard(Timestamp startDate, Timestamp endDate) throws ErrMessageException {
        if (startDate == null) {
            startDate = Timestamp.valueOf(LocalDateTime.now());
        }
        if (endDate == null) {
            endDate = Timestamp.valueOf(LocalDateTime.now());
        }
        if (startDate.after(endDate)) {
            throw new ErrMessageException("The startDate must be less than or equal to the endDate.");
        }

        // Lấy thông tin người dùng và ví
        User user = userService.getLoginUser();
        Customer customer = customerRepository.findByUserId(user.getId());
        Wallet wallet = walletRepository.findWalletByOwnerId(customer.getId());
        if (wallet == null) {
            throw new ErrMessageException("Wallet not found for customer ID: " + customer.getId());
        }

        // Tính doanh thu và chi phí cho từng ngày trong khoảng thời gian
        List<Timestamp> allDates = new ArrayList<>();
        Timestamp currentDate = startDate;

        while (!currentDate.toLocalDateTime().toLocalDate().isAfter(endDate.toLocalDateTime().toLocalDate())) {
            allDates.add(currentDate);
            currentDate = Timestamp.valueOf(currentDate.toLocalDateTime().plusDays(1));
        }

        Map<Date, Double> revenueMap = new LinkedHashMap<>();
        Map<Date, Double> costMap = new LinkedHashMap<>();

        for (Timestamp date : allDates) {
            LocalDate localDate = date.toLocalDateTime().toLocalDate();

            Double revenue = walletTransactionRepository.sumMoneyByCustomerIdAndDateAndMoneyGreaterThan(wallet.getId(), localDate);
            Double cost = walletTransactionRepository.sumMoneyByCustomerIdAndDateAndMoneyLessThan(wallet.getId(), localDate);

            if (revenue == null) {
                revenue = 0.0;
            }
            if (cost == null) {
                cost = 0.0;
            }


            // Lưu vào map
            revenueMap.put(new Date(date.getTime()), revenue);
            costMap.put(new Date(date.getTime()), cost);
        }

        // Tạo danh sách DTO từ map
        List<CustomerMoneyDashboardDto.RevenueCostByDateDto> revenueCostByDateDtos = new ArrayList<>();
        revenueMap.forEach((date, revenue) -> {
            Double cost = costMap.getOrDefault(date, 0.0);
            revenueCostByDateDtos.add(CustomerMoneyDashboardDto.RevenueCostByDateDto.builder()
                    .date(date)
                    .revenueByDate(revenue)
                    .revenueByCosts(cost)
                    .build());
        });
        Double allRevenue = walletTransactionRepository.sumMoneyByCustomerIdAndMoneyGreaterThan(wallet.getId());
        Double allCosts = walletTransactionRepository.sumMoneyByCustomerIdAndMoneyLessThan(wallet.getId());
        return CustomerMoneyDashboardDto.builder()
                .totalRevenue(allRevenue)
                .totalCosts(allCosts)
                .revenueCostByDateDtos(revenueCostByDateDtos)
                .build();
    }

    @Override
    public AdminDashboardDto getAdminDashboard() {

        Long totalUsers = userRepository.totalUser();
        Long totalStaffs= timeshareCompanyStaffRepository.totalStaffs();
        Long totalCustomers = userRepository.getTotalCustomers();
        Long totalTimeshareCompany= userRepository.totalTimeshareCompany();
        Long totalSystemStaff=userRepository.totalSystemStaff();
        Long totalAdmin=userRepository.totalAdmin();
        Long allUser = totalUsers + totalStaffs;
        return AdminDashboardDto.builder()
                .totalUser(allUser)
                .totalCustomer(totalCustomers)
                .totalTimeshareCompany(totalTimeshareCompany)
                .totalTimeshareStaff(totalStaffs)
                .totalSystemStaff(totalSystemStaff)
                .totalAdmin(totalAdmin)
                .build();
    }

    @Override
    public AdminDashboardBalanceDto getAdminDashboardRevuenue() throws ErrMessageException {
        Float membership;
        Float rentalPosting;
        Float exchangePosting;
        Float total;
        try{
            membership = Math.abs(walletTransactionRepository.calculateTotalMembershipTransactions());
            rentalPosting = Math.abs(walletTransactionRepository.calculateTotalRentalPostingTransactions());
            exchangePosting = Math.abs(walletTransactionRepository.calculateTotalExchangePostingTransactions());
            total = Math.abs(walletTransactionRepository.calculateNormalMoneyTotal()) + walletTransactionRepository.calculatePackageFourMoneyTotal();
        }catch (Exception e){
            throw new ErrMessageException("fail for calculate money");
        }
        AdminDashboardBalanceDto adminDashboardBalanceDto = AdminDashboardBalanceDto.builder()
                .membershipRevuenue(membership)
                .rentalPostingRevuenue(rentalPosting)
                .exchangePostingRevuenue(exchangePosting)
                .totalRevuenue(total)
                .build();
        return adminDashboardBalanceDto;
    }


}
