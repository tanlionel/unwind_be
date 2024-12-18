package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.enums.ExchangeBookingEnum;
import com.capstone.unwind.enums.ExchangePostingEnum;
import com.capstone.unwind.enums.ExchangeRequestEnum;
import com.capstone.unwind.enums.WalletTransactionEnum;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.CustomerDTO.*;
import com.capstone.unwind.model.UserDTO.UpdatePasswordRequestDTO;
import com.capstone.unwind.model.WalletDTO.*;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.CustomerService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import com.capstone.unwind.service.ServiceInterface.WalletService;
import io.swagger.models.auth.In;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImplement implements CustomerService {
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final CustomerMapper customerMapper;
    @Autowired
    private final WalletRepository walletRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final WalletService walletService;
    @Autowired
    private final MembershipRepository membershipRepository;
    @Autowired
    private final WalletTransactionMapper walletTransactionMapper;
    @Autowired
    private final WalletMapper walletMapper;
    @Autowired
    private final CustomerInitMapper customerInitMapper;
    @Autowired
    private final RentalPackageRepository rentalPackageRepository;
    @Autowired
    private RentalPostingRepository rentalPostingRepository;
    @Autowired
    private ProfileMapper profileMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final ExchangePackageRepository exchangePackageRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final ExchangeRequestRepository exchangeRequestRepository;
    @Autowired
    private final ExchangePostingRepository exchangePostingRepository;
    @Autowired
    private final ExchangeBookingRepository exchangeBookingRepository;

    @Override
    public CustomerDto createCustomer(CustomerRequestDto customerRequestDto) throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        if (user==null) throw new OptionalNotFoundException("User id not found");
        Customer customer = Customer.builder()
                .fullName(customerRequestDto.getFullName())
                .dob(customerRequestDto.getDob())
                .address(customerRequestDto.getAddress())
                .gender(customerRequestDto.getGender())
                .phone(customerRequestDto.getPhone())
                .isActive(true)
                .user(user)
                .build();
        Customer customerInDb = customerRepository.save(customer);
        CustomerDto customerDto = customerMapper.toDto(customerInDb);
        float moneyInit = 0.0f;
        Wallet wallet = Wallet.builder()
                .owner(customerInDb)
                .availableMoney(moneyInit)
                .isActive(true)
                .type("CUSTOMER_WALLET")
                .build();
        walletRepository.save(wallet);

        //check customer member
        boolean isMember = checkIsMember(customerInDb);
        customerDto.setIsMember(isMember);
        return customerDto;
    }

    @Override
    public CustomerDto getCustomerByUserId(Integer userId) throws OptionalNotFoundException {
        Customer customer = customerRepository.findByUserId(userId);
        if (customer==null) throw new OptionalNotFoundException("Customer not found");
        CustomerDto customerDto = customerMapper.toDto(customer);
        boolean isMember = checkIsMember(customer);
        customerDto.setIsMember(isMember);
        return customerDto;
    }

    @Override
    public CustomerInitDto getLoginCustomer() throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        if (user.getCustomer()==null) throw new OptionalNotFoundException("not init customer yet");
        CustomerInitDto customerInitDto = customerInitMapper.toDto(user.getCustomer());
        boolean isMember = checkIsMember(user.getCustomer());
        customerInitDto.setIsMember(isMember);
        return customerInitDto;
    }

    @Override
    public CustomerDto getCustomerByCustomerId(Integer customerId) throws OptionalNotFoundException {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) throw new OptionalNotFoundException("Customer not found");
        CustomerDto customerDto = customerMapper.toDto(customer.get());
        boolean isMember = checkIsMember(customer.get());
        customerDto.setIsMember(isMember);
        return customerDto;
    }

    @Override
    public MembershipResponseDto extendMembershipVNPAY(UUID uuid, Integer membership_id) throws OptionalNotFoundException, ErrMessageException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        Membership membership = membershipRepository.findById(membership_id).get();
        if (membership == null) throw new OptionalNotFoundException("Not found membership package");

        //extend membership
        Customer customer = user.getCustomer();
        customer.setMembership(membership);
        customer.setMemberPurchaseDate(LocalDate.now());
        if(customer.getMemberExpiryDate()==null){
            customer.setMemberExpiryDate(LocalDate.now().plusMonths(membership.getDuration()));
        }
        else customer.setMemberExpiryDate(customer.getMemberExpiryDate().plusMonths(membership.getDuration()));

        //update transaction
        WalletTransaction walletTransaction = walletService.updateTransactionMembershipByVNPAY(uuid,membership_id);
        Customer customerInDB = customerRepository.save(customer);

        MembershipResponseDto membershipResponseDto = MembershipResponseDto.builder()
                .walletTransactionDto(walletTransactionMapper.toDto(walletTransaction))
                .customerId(customerInDB.getId())
                .build();

        return membershipResponseDto;
    }

    @Override
    public WalletTransactionDto depositMoneyVNPAY(UUID uuid) throws OptionalNotFoundException, ErrMessageException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");

        //update transaction
        WalletTransaction walletTransaction = walletService.updateTransactionDepositMoneyByVNPAY(uuid);
        Wallet wallet = walletRepository.findWalletByOwnerId(user.getCustomer().getId());
        //set available money
        wallet.setAvailableMoney(wallet.getAvailableMoney()+walletTransaction.getMoney());
        Wallet walletInDb = walletRepository.save(wallet);

        return walletTransactionMapper.toDto(walletTransaction);
    }

    @Override
    public MembershipResponseDto extendMembershipWallet(Integer membershipId) throws OptionalNotFoundException, ErrMessageException {
        //get user
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        Membership membership = membershipRepository.findById(membershipId).get();
        if (membership == null) throw new OptionalNotFoundException("Not found membership package");

        if (membership.getPrice()>user.getCustomer().getWallet().getAvailableMoney()) throw new ErrMessageException("not enough money");

        //minus money and create transaction
        user.getCustomer().getWallet().setAvailableMoney(user.getCustomer().getWallet().getAvailableMoney()-membership.getPrice());
        WalletTransaction walletTransaction = walletService.createTransactionWallet(0,membership.getPrice(),"WALLET");
        String description = "Thanh toán membership "+membership.getDuration()+" tháng";
        String transactionType = "MEMBERSHIP";
        WalletTransaction walletTransactionAfterUpdate = walletService.updateTransaction(walletTransaction.getId(),description,transactionType);
        walletRepository.save(user.getCustomer().getWallet());

        //extend membership
        Customer customer = user.getCustomer();
        customer.setMembership(membership);
        customer.setMemberPurchaseDate(LocalDate.now());
        if(customer.getMemberExpiryDate()==null){
            customer.setMemberExpiryDate(LocalDate.now().plusMonths(membership.getDuration()));
        }
        else customer.setMemberExpiryDate(customer.getMemberExpiryDate().plusMonths(membership.getDuration()));
        Customer customerInDB = customerRepository.save(customer);

        MembershipResponseDto membershipResponseDto = MembershipResponseDto.builder()
                .walletTransactionDto(walletTransactionMapper.toDto(walletTransactionAfterUpdate))
                .customerId(customerInDB.getId())
                .build();

        return membershipResponseDto;
    }

    @Override
    public WalletTransactionDto paymentRentalPostingVNPAY(UUID uuid, Integer rentalPackageId) throws OptionalNotFoundException, ErrMessageException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");

        Optional<RentalPackage> rentalPackage =rentalPackageRepository.findById(rentalPackageId);
        if (!rentalPackage.isPresent()) throw new OptionalNotFoundException("Not found rental pacakge");
        String description = "Thanh toán đăng bài " + rentalPackage.get().getRentalPackageName();
        String transactionType = "RENTALPOSTING";
        //update transaction
        WalletTransaction walletTransaction = walletService.updateTransaction(uuid,description,transactionType);

        return walletTransactionMapper.toDto(walletTransaction);
    }

    @Override
    public WalletTransactionDto paymentRentalPostingWallet(Integer rentalPackageId) throws OptionalNotFoundException, ErrMessageException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        Optional<RentalPackage> rentalPackage =rentalPackageRepository.findById(rentalPackageId);
        if (!rentalPackage.isPresent()) throw new OptionalNotFoundException("Not found rental package");

        if (rentalPackage.get().getPrice()>user.getCustomer().getWallet().getAvailableMoney()) throw new ErrMessageException("not enough money");
        user.getCustomer().getWallet().setAvailableMoney(user.getCustomer().getWallet().getAvailableMoney()-rentalPackage.get().getPrice());
        WalletTransaction walletTransaction = walletService.createTransactionWallet(0,rentalPackage.get().getPrice(), "WALLET");
        walletRepository.save(user.getCustomer().getWallet());


        String description = "Thanh toán đăng bài " + rentalPackage.get().getRentalPackageName();
        String transactionType = "RENTALPOSTING";
        //update transaction
        WalletTransaction walletTransactionAfterUpdate = walletService.updateTransaction(walletTransaction.getId(),description,transactionType);

        return walletTransactionMapper.toDto(walletTransactionAfterUpdate);
    }

    @Override
    public WalletTransactionDto paymentRentalBookingWallet(Integer postingId) throws OptionalNotFoundException, ErrMessageException {
        RentalPosting rentalPosting = rentalPostingRepository.findByIdAndIsActive(postingId).orElseThrow(()-> new OptionalNotFoundException("not found posting"));
        if (rentalPosting.getIsBookable()) throw new ErrMessageException("Posting has booked yet by another customer");
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");

        if ((rentalPosting.getPricePerNights()*rentalPosting.getNights())>user.getCustomer().getWallet().getAvailableMoney()) throw new ErrMessageException("not enough money");
        user.getCustomer().getWallet().setAvailableMoney(user.getCustomer().getWallet().getAvailableMoney()-(rentalPosting.getNights()*rentalPosting.getPricePerNights()));
        WalletTransaction walletTransaction = walletService.createTransactionWallet(0,(rentalPosting.getNights()*rentalPosting.getPricePerNights()), "WALLET");
        walletRepository.save(user.getCustomer().getWallet());


        String description = "Thanh toán đặt chỗ timeshare cho thuê ";
        String transactionType = "RENTALBOOKING";
        //update transaction
        WalletTransaction walletTransactionAfterUpdate = walletService.updateTransaction(walletTransaction.getId(),description,transactionType);

        return walletTransactionMapper.toDto(walletTransactionAfterUpdate);
    }

    @Override
    public WalletTransactionDto paymentRentalBookingVNPAY(UUID uuid, Integer postingId) throws OptionalNotFoundException, ErrMessageException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");

        RentalPosting rentalPosting = rentalPostingRepository.findByIdAndIsActive(postingId).orElseThrow(()-> new OptionalNotFoundException("not found posting"));
        if (rentalPosting.getIsBookable()) throw new ErrMessageException("Posting has booked yet by another customer");
        String description = "Thanh toán đặt chỗ timeshare cho thuê ";
        String transactionType = "RENTALBOOKING";
        //update transaction
        WalletTransaction walletTransaction = walletService.updateTransaction(uuid,description,transactionType);

        return walletTransactionMapper.toDto(walletTransaction);
    }

    public boolean checkIsMember(Customer customer){
        boolean isMember = customer.getMemberExpiryDate()!=null && customer.getMemberExpiryDate().isAfter(LocalDate.now());
        return isMember;
    }


    @Override
    public ProfileDto getProfile() throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        Customer customer = customerRepository.findByUserId(user.getId());
        if (customer == null) {
            throw new OptionalNotFoundException("Customer does not exist with userId: " + user.getId());
        }
        ProfileDto profileDto = profileMapper.toDto(customer);
        boolean isMember = checkIsMember(customer);
        profileDto.setIsMember(isMember);
        return profileDto;
    }
    @Override
    public ProfileDto getCustomerById(Integer id) throws OptionalNotFoundException {
        Customer customer = customerRepository.findByUserId(id);
        if (customer == null) {
            throw new OptionalNotFoundException("Customer does not exist with userId: " + id);
        }
        return profileMapper.toDto(customer);
    }
    @Override
    public ProfileDto updateProfile(UpdateProfileDto profileUpdateDto) throws OptionalNotFoundException, ErrMessageException {
        User user = userService.getLoginUser();
        Customer customer = customerRepository.findByUserId(user.getId());
        if (customer == null) {
            throw new OptionalNotFoundException("Customer does not exist with userId: " + user.getId());
        }
        //check valid
        validateProfileData(profileUpdateDto);

        customer.setFullName(profileUpdateDto.getFullName());
        customer.setAvatar(profileUpdateDto.getAvatar());
        customer.setDob(profileUpdateDto.getDob());
        customer.setAddress(profileUpdateDto.getAddress());
        customer.setGender(profileUpdateDto.getGender());
        customer.setPhone(profileUpdateDto.getPhone());

        customerRepository.save(customer);
        return profileMapper.toDto(customer);
    }
    @Override
    public WalletTransactionDto paymentExchangePostingWallet(Integer packageId) throws OptionalNotFoundException, ErrMessageException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        Optional<ExchangePackage> exchangePackage =exchangePackageRepository.findById(packageId);
        if (!exchangePackage.isPresent()) throw new OptionalNotFoundException("Not found exchange package");

        if (exchangePackage.get().getPrice()>user.getCustomer().getWallet().getAvailableMoney()) throw new ErrMessageException("not enough money");
        user.getCustomer().getWallet().setAvailableMoney(user.getCustomer().getWallet().getAvailableMoney()-exchangePackage.get().getPrice());
        WalletTransaction walletTransaction = walletService.createTransactionWallet(0,exchangePackage.get().getPrice(), "WALLET");
        walletRepository.save(user.getCustomer().getWallet());


        String description = "Thanh toán đăng bài " + exchangePackage.get().getPackageName();
        String transactionType = "EXCHANGEPOSTING";
        //update transaction
        WalletTransaction walletTransactionAfterUpdate = walletService.updateTransaction(walletTransaction.getId(),description,transactionType);

        return walletTransactionMapper.toDto(walletTransactionAfterUpdate);
    }

    @Override
    public WalletTransactionDto paymentExchangePostingVNPAY(UUID uuid, Integer packageId) throws OptionalNotFoundException, ErrMessageException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");

        Optional<ExchangePackage> exchangePackage =exchangePackageRepository.findById(packageId);
        if (!exchangePackage.isPresent()) throw new OptionalNotFoundException("Not found exchange package");
        String description = "Thanh toán đăng bài " + exchangePackage.get().getPackageName();
        String transactionType = "EXCHANGEPOSTING";
        //update transaction
        WalletTransaction walletTransaction = walletService.updateTransaction(uuid,description,transactionType);

        return walletTransactionMapper.toDto(walletTransaction);
    }

    @Override
    public boolean checkUserExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private void validateProfileData(UpdateProfileDto profileUpdateDto) throws ErrMessageException {
        if (profileUpdateDto.getDob() != null) {
            LocalDate today = LocalDate.now();
            LocalDate dobThreshold = today.minusYears(18);
            if (profileUpdateDto.getDob().isAfter(dobThreshold)) {
                throw new ErrMessageException("Date of birth must indicate an age over 18.");
            }
        }

        if (profileUpdateDto.getPhone() != null) {
            String phone = profileUpdateDto.getPhone();
            if (!phone.matches("^0\\d{9}$")) {
                throw new ErrMessageException("Phone number must be 10 digits and start with 0.");
            }
        }
    }
    @Override
    public void changePassword( UpdatePasswordRequestDTO updatePasswordRequest) throws ErrMessageException {
        User user = userService.getLoginUser();


        if (!passwordEncoder.matches(updatePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new ErrMessageException("Current password is incorrect");
        }

        if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmNewPassword())) {
            throw new ErrMessageException("New password and confirmation password do not match");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));

        userRepository.save(user);
    }

    @Override
    @Transactional
    public WalletTransactionDto paymentExchangeRequestVNPAY(UUID uuid, Integer requestId) throws OptionalNotFoundException, ErrMessageException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        ExchangeRequest exchangeRequest = exchangeRequestRepository.findByIdAndIsActive(requestId).orElseThrow(()-> new OptionalNotFoundException("not found exchange request"));
        if (exchangeRequest.getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingRenterPayment))){
            Customer owner = exchangeRequest.getExchangePosting().getOwner();
            //owner.getWallet().setAvailableMoney(owner.getWallet().getAvailableMoney()+Math.abs(exchangeRequest.getPriceValuation()));
            String descriptionOwner = "Nhận tiền thanh toán bù trừ trao đổi timeshare";
            String transactionTypeOwner = String.valueOf(WalletTransactionEnum.EXCHANGEREQUEST_VALUATION);
            WalletTransaction walletTransactionOwner = walletService.refundMoneyToCustomer(owner.getId(),0,Math.abs(exchangeRequest.getPriceValuation()),"WALLET",descriptionOwner,transactionTypeOwner);
            walletRepository.save(owner.getWallet());
        }else {
            Customer renter = exchangeRequest.getOwner();
            //renter.getWallet().setAvailableMoney(renter.getWallet().getAvailableMoney()+Math.abs(exchangeRequest.getPriceValuation()));
            String descriptionRenter = "Nhận tiền thanh toán bù trừ trao đổi timeshare";
            String transactionTypeRenter = String.valueOf(WalletTransactionEnum.EXCHANGEREQUEST_VALUATION);
            WalletTransaction walletTransactionOwner = walletService.refundMoneyToCustomer(renter.getId(),0,Math.abs(exchangeRequest.getPriceValuation()),"WALLET",descriptionRenter,transactionTypeRenter);
            walletRepository.save(renter.getWallet());
        }
        String description = "Thanh toán bù trừ trao đổi timeshare";
        String transactionType = String.valueOf(WalletTransactionEnum.EXCHANGEREQUEST_VALUATION);
        //update transaction
        WalletTransaction walletTransaction = walletService.updateTransaction(uuid,description,transactionType);
        exchangeRequest.setStatus(String.valueOf(ExchangeRequestEnum.Complete));
        exchangeRequestRepository.save(exchangeRequest);
        ExchangePosting exchangePosting = exchangeRequest.getExchangePosting();
        exchangePosting.setStatus(String.valueOf(ExchangePostingEnum.Completed));
        exchangePostingRepository.save(exchangePosting);
        exchangeRequestRepository.updateOtherRequestsStatusByExchangePosting(
                exchangeRequest.getExchangePosting().getId(),
                exchangeRequest.getId()
        );

        Period period = Period.between(exchangeRequest.getStartDate(), exchangeRequest.getEndDate());
        int days = period.getDays() + 1;
        ExchangeBooking requesterBooking =  ExchangeBooking.builder()
                .isActive(true)
                .checkoutDate(exchangeRequest.getEndDate())
                .checkinDate(exchangeRequest.getStartDate())
                .status(String.valueOf(ExchangeBookingEnum.Booked))
                .exchangeRequest(exchangeRequest)
                .exchangePosting(exchangeRequest.getExchangePosting())
                .renter(exchangeRequest.getExchangePosting().getOwner())
                .roomInfo(exchangeRequest.getRoomInfo())
                .timeshare(exchangeRequest.getTimeshare())
                .nights(days)
                .isPrimaryGuest(false)
                .isFeedback(false)
                .build();
        exchangeBookingRepository.save(requesterBooking);

        ExchangeBooking ownerBooking = ExchangeBooking.builder()
                .timeshare(exchangeRequest.getExchangePosting().getTimeshare())
                .roomInfo(exchangeRequest.getExchangePosting().getRoomInfo())
                .renter(exchangeRequest.getOwner())
                .exchangePosting(exchangeRequest.getExchangePosting())
                .exchangeRequest(exchangeRequest)
                .status(String.valueOf(ExchangeBookingEnum.Booked))
                .checkoutDate(exchangeRequest.getExchangePosting().getCheckoutDate())
                .checkinDate(exchangeRequest.getExchangePosting().getCheckinDate())
                .nights(exchangeRequest.getExchangePosting().getNights())
                .isFeedback(false)
                .isPrimaryGuest(false)
                .isActive(true)
                .build();
        exchangeBookingRepository.save(ownerBooking);

        return walletTransactionMapper.toDto(walletTransaction);
    }

    @Override
    @Transactional
    public WalletTransactionDto paymentExchangeRequestWallet(Integer exchangeRequestId) throws OptionalNotFoundException, ErrMessageException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        ExchangeRequest exchangeRequest = exchangeRequestRepository.findByIdAndIsActive(exchangeRequestId).orElseThrow(()-> new OptionalNotFoundException("not found exchange request"));

        WalletTransaction walletTransaction ;
        if (Math.abs(exchangeRequest.getPriceValuation())>user.getCustomer().getWallet().getAvailableMoney()) throw new ErrMessageException("not enough money");

        if (exchangeRequest.getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingRenterPayment))) {
            user.getCustomer().getWallet().setAvailableMoney(user.getCustomer().getWallet().getAvailableMoney()-Math.abs(exchangeRequest.getPriceValuation()));
             walletTransaction = walletService.createTransactionWallet(0,Math.abs(exchangeRequest.getPriceValuation()), "WALLET");
            walletRepository.save(user.getCustomer().getWallet());
        }
        else {
            exchangeRequest.getExchangePosting().getOwner().getWallet().setAvailableMoney(exchangeRequest.getExchangePosting().getOwner().getWallet().getAvailableMoney()-Math.abs(exchangeRequest.getPriceValuation()));
            walletTransaction = walletService.createTransactionWallet(0,Math.abs(exchangeRequest.getPriceValuation()), "WALLET");
            walletRepository.save(exchangeRequest.getExchangePosting().getOwner().getWallet());
        }


        if (exchangeRequest.getStatus().equals(String.valueOf(ExchangeRequestEnum.PendingRenterPayment))){
            Customer owner = exchangeRequest.getExchangePosting().getOwner();
            //owner.getWallet().setAvailableMoney(owner.getWallet().getAvailableMoney()+Math.abs(exchangeRequest.getPriceValuation()));
            String descriptionOwner = "Nhận tiền thanh toán bù trừ trao đổi timeshare";
            String transactionTypeOwner = String.valueOf(WalletTransactionEnum.EXCHANGEREQUEST_VALUATION);
            WalletTransaction walletTransactionOwner = walletService.refundMoneyToCustomer(owner.getId(),0,Math.abs(exchangeRequest.getPriceValuation()),"WALLET",descriptionOwner,transactionTypeOwner);
            walletRepository.save(owner.getWallet());
        }else {
            Customer renter = exchangeRequest.getOwner();
            //renter.getWallet().setAvailableMoney(renter.getWallet().getAvailableMoney()+Math.abs(exchangeRequest.getPriceValuation()));
            String descriptionRenter = "Nhận tiền thanh toán bù trừ trao đổi timeshare";
            String transactionTypeRenter = String.valueOf(WalletTransactionEnum.EXCHANGEREQUEST_VALUATION);
            WalletTransaction walletTransactionRenter = walletService.refundMoneyToCustomer(renter.getId(),0,Math.abs(exchangeRequest.getPriceValuation()),"WALLET",descriptionRenter,transactionTypeRenter);
            walletRepository.save(renter.getWallet());
        }

        String description = "Thanh toán bù trừ trao đổi timeshare ";
        String transactionType = String.valueOf(WalletTransactionEnum.EXCHANGEREQUEST_VALUATION);
        //update transaction
        WalletTransaction walletTransactionAfterUpdate = walletService.updateTransaction(walletTransaction.getId(),description,transactionType);
        exchangeRequest.setStatus(String.valueOf(ExchangeRequestEnum.Complete));
        exchangeRequestRepository.save(exchangeRequest);
        ExchangePosting exchangePosting = exchangeRequest.getExchangePosting();
        exchangePosting.setStatus(String.valueOf(ExchangePostingEnum.Completed));
        exchangePostingRepository.save(exchangePosting);

        walletRepository.save(user.getCustomer().getWallet());
        walletRepository.save(exchangeRequest.getExchangePosting().getOwner().getWallet());


        exchangeRequestRepository.updateOtherRequestsStatusByExchangePosting(
                exchangeRequest.getExchangePosting().getId(),
                exchangeRequest.getId()
        );



        Period period = Period.between(exchangeRequest.getStartDate(), exchangeRequest.getEndDate());
        int days = period.getDays() + 1;
        ExchangeBooking requesterBooking =  ExchangeBooking.builder()
                .isActive(true)
                .checkoutDate(exchangeRequest.getEndDate())
                .checkinDate(exchangeRequest.getStartDate())
                .status(String.valueOf(ExchangeBookingEnum.Booked))
                .exchangeRequest(exchangeRequest)
                .exchangePosting(exchangeRequest.getExchangePosting())
                .renter(exchangeRequest.getExchangePosting().getOwner())
                .roomInfo(exchangeRequest.getRoomInfo())
                .timeshare(exchangeRequest.getTimeshare())
                .nights(days)
                .isPrimaryGuest(false)
                .isFeedback(false)
                .build();
        exchangeBookingRepository.save(requesterBooking);

        ExchangeBooking ownerBooking = ExchangeBooking.builder()
                .timeshare(exchangeRequest.getExchangePosting().getTimeshare())
                .roomInfo(exchangeRequest.getExchangePosting().getRoomInfo())
                .renter(exchangeRequest.getOwner())
                .exchangePosting(exchangeRequest.getExchangePosting())
                .exchangeRequest(exchangeRequest)
                .status(String.valueOf(ExchangeBookingEnum.Booked))
                .checkoutDate(exchangeRequest.getExchangePosting().getCheckoutDate())
                .checkinDate(exchangeRequest.getExchangePosting().getCheckinDate())
                .nights(exchangeRequest.getExchangePosting().getNights())
                .isFeedback(false)
                .isPrimaryGuest(false)
                .isActive(true)
                .build();
        exchangeBookingRepository.save(ownerBooking);
        return walletTransactionMapper.toDto(walletTransactionAfterUpdate);
    }
}
