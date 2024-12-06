package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.Customer;
import com.capstone.unwind.entity.TimeshareCompany;
import com.capstone.unwind.entity.User;
import com.capstone.unwind.entity.Wallet;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.DashboardDTO.CustomerDashboardDto;
import com.capstone.unwind.model.DashboardDTO.CustomerMoneyDashboardDto;
import com.capstone.unwind.model.DashboardDTO.RevenueCostByDateDto;
import com.capstone.unwind.model.TotalPackageDTO.PackageDashboardDto;
import com.capstone.unwind.model.TotalPackageDTO.TotalPackageDto;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.DashboardService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    public PackageDashboardDto getTotalPackageByDate(Timestamp startDate, Timestamp endDate) throws ErrMessageException {

        // Set default startDate and endDate if null
        if (startDate == null) {
            startDate = Timestamp.valueOf(LocalDateTime.now().toLocalDate().atStartOfDay());
        }

        if (endDate == null) {
            endDate = Timestamp.valueOf(LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX));
        }

        // Validate the date range
        if (startDate.after(endDate)) {
            throw new ErrMessageException("The startDate must be less than or equal to the endDate.");
        }

        // Get total package counts for each day (returns a list of Object[] with count and date)
        List<Object[]> rentalPackageResults = rentalPostingRepository.countRentalPackageByDateRange(startDate, endDate);
        List<Object[]> exchangePackageResults = exchangePostingRepository.countExchangePackageByDateRange(startDate, endDate);
        List<Object[]> membershipPackageResults = walletTransactionRepository.countMembershipPackageByDateRange(startDate, endDate);

        // Create a list of all dates in the range
        List<Timestamp> allDates = new ArrayList<>();
        Timestamp currentDate = startDate;

        while (currentDate.before(endDate) || currentDate.equals(endDate)) {
            allDates.add(currentDate);
            LocalDateTime nextDay = currentDate.toLocalDateTime().plusDays(1);
            currentDate = Timestamp.valueOf(nextDay);
        }
        allDates.sort(Comparator.naturalOrder());

        // Map to store package data by date
        Map<String, TotalPackageDto> packageByDateMap = new LinkedHashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        // Initialize values for each date
        for (Timestamp date : allDates) {
            packageByDateMap.put(dateFormat.format(date),
                    TotalPackageDto.builder()
                            .totalRentalPackage(0L)
                            .totalExchangePackage(0L)
                            .totalMemberShip(0L)
                            .build());
        }

        // Map the results to the corresponding dates (handle both java.sql.Date and java.sql.Timestamp)
        for (Object[] rentalResult : rentalPackageResults) {
            Object dateObj = rentalResult[1];
            Timestamp rentalDate = convertToTimestamp(dateObj);
            Long rentalCount = (Long) rentalResult[0];

            packageByDateMap.computeIfPresent(dateFormat.format(rentalDate), (key, value) -> {
                value.setTotalRentalPackage(rentalCount);
                return value;
            });
        }

        for (Object[] exchangeResult : exchangePackageResults) {
            Object dateObj = exchangeResult[1];
            Timestamp exchangeDate = convertToTimestamp(dateObj);
            Long exchangeCount = (Long) exchangeResult[0];

            packageByDateMap.computeIfPresent(dateFormat.format(exchangeDate), (key, value) -> {
                value.setTotalExchangePackage(exchangeCount);
                return value;
            });
        }

        for (Object[] membershipResult : membershipPackageResults) {
            Object dateObj = membershipResult[1];
            Timestamp membershipDate = convertToTimestamp(dateObj);
            Long membershipCount = (Long) membershipResult[0];

            packageByDateMap.computeIfPresent(dateFormat.format(membershipDate), (key, value) -> {
                value.setTotalMemberShip(membershipCount);
                return value;
            });
        }


        for (Map.Entry<String, TotalPackageDto> entry : packageByDateMap.entrySet()) {
            TotalPackageDto dto = entry.getValue();

            dto.setTotalRentalPackage(Optional.ofNullable(dto.getTotalRentalPackage()).orElse(0L));
            dto.setTotalExchangePackage(Optional.ofNullable(dto.getTotalExchangePackage()).orElse(0L));
            dto.setTotalMemberShip(Optional.ofNullable(dto.getTotalMemberShip()).orElse(0L));
        }

        // Return the final DTO
        return PackageDashboardDto.builder()
                .packageByDateMap(packageByDateMap)
                .build();
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
            startDate = Timestamp.valueOf(LocalDateTime.now().toLocalDate().atStartOfDay());
        }

        if (endDate == null) {
            endDate = Timestamp.valueOf(LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX));
        }

        if (startDate.after(endDate)) {
            throw new ErrMessageException("The startDate must be less than or equal to the endDate.");
        }

        // Get the logged-in user and their customer information
        User user = userService.getLoginUser();
        Customer customer = customerRepository.findByUserId(user.getId());

        Wallet wallet = walletRepository.findWalletByOwnerId(customer.getId());
        if (wallet == null) {
            throw new ErrMessageException("Wallet not found for customer ID: " + customer.getId());
        }

        Long totalRevenue = walletTransactionRepository.sumMoneyByCustomerIdAndMoneyGreaterThan(wallet.getId());
        Long totalCosts = walletTransactionRepository.sumMoneyByCustomerIdAndMoneyLessThan(wallet.getId());

        List<Object[]> revenueByDate = walletTransactionRepository.sumMoneyByCustomerIdAndDateRangeAndMoneyGreaterThan(wallet.getId(), startDate, endDate);
        List<Object[]> costsByDate = walletTransactionRepository.sumMoneyByCustomerIdAndDateRangeAndMoneyLessThan(wallet.getId(), startDate, endDate);

        List<Timestamp> allDates = new ArrayList<>();
        Timestamp currentDate = startDate;
        while (currentDate.before(endDate) || currentDate.equals(endDate)) {
            allDates.add(currentDate);
            LocalDateTime nextDay = currentDate.toLocalDateTime().plusDays(1);
            currentDate = Timestamp.valueOf(nextDay);
        }

        allDates.sort(Comparator.naturalOrder());

        Map<String, RevenueCostByDateDto> revenueCostByDateMap = new LinkedHashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


        Map<String, Long> revenueMap = new HashMap<>();
        Map<String, Long> costsMap = new HashMap<>();

        // Populate revenue map
        for (Object[] revenueResult : revenueByDate) {
            Object dateObj = revenueResult[1];
            Timestamp revenueDate = convertToTimestamp(dateObj);
            Double revenueValue = (Double) revenueResult[0];

            String formattedDate = dateFormat.format(revenueDate);
            revenueMap.put(formattedDate, revenueValue != null ? revenueValue.longValue() : 0L);
        }

        for (Object[] costsResult : costsByDate) {
            Object dateObj = costsResult[1];
            Timestamp costDate = convertToTimestamp(dateObj);
            Double costValue = (Double) costsResult[0];

            String formattedDate = dateFormat.format(costDate);
            costsMap.put(formattedDate, costValue != null ? costValue.longValue() : 0L);
        }

        // Populate the revenueCostByDateMap
        for (Timestamp date : allDates) {
            String formattedDate = dateFormat.format(date);
            Long revenueValue = revenueMap.getOrDefault(formattedDate, 0L);
            Long costValue = costsMap.getOrDefault(formattedDate, 0L);

            revenueCostByDateMap.put(formattedDate,
                    RevenueCostByDateDto.builder()
                            .revenueByDate(revenueValue)
                            .revenueByCosts(costValue)
                            .build());
        }

        // Return the final DTO
        return CustomerMoneyDashboardDto.builder()
                .totalRevenue(Optional.ofNullable(totalRevenue).orElse(0L))
                .totalCosts(Optional.ofNullable(totalCosts).orElse(0L))     // Default to 0 if null
                .revenueCostByDateMap(revenueCostByDateMap)
                .build();
    }


}
