package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.CustomerDTO.*;
import com.capstone.unwind.model.WalletDTO.*;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.CustomerService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import com.capstone.unwind.service.ServiceInterface.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public boolean checkIsMember(Customer customer){
        boolean isMember = customer.getMemberExpiryDate()!=null && customer.getMemberExpiryDate().isAfter(LocalDate.now());
        return isMember;
    }
}
