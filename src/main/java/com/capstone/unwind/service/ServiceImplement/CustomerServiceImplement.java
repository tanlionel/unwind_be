package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.CustomerDTO.CustomerDto;
import com.capstone.unwind.model.CustomerDTO.CustomerMapper;
import com.capstone.unwind.model.CustomerDTO.CustomerRequestDto;
import com.capstone.unwind.model.WalletDTO.MembershipResponseDto;
import com.capstone.unwind.model.WalletDTO.WalletTransactionMapper;
import com.capstone.unwind.repository.CustomerRepository;
import com.capstone.unwind.repository.MembershipRepository;
import com.capstone.unwind.repository.UserRepository;
import com.capstone.unwind.repository.WalletRepository;
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
        customer.setMemberExpiryDate(LocalDate.now().plusMonths(membership.getDuration()));

        //update transaction
        WalletTransaction walletTransaction = walletService.updateTransactionMembershipByVNPAY(uuid,membership_id);
        Customer customerInDB = customerRepository.save(customer);

        MembershipResponseDto membershipResponseDto = MembershipResponseDto.builder()
                .walletTransactionDto(walletTransactionMapper.toDto(walletTransaction))
                .customerId(customerInDB.getId())
                .build();

        return membershipResponseDto;
    }

    public boolean checkIsMember(Customer customer){
        boolean isMember = customer.getMemberExpiryDate()!=null && customer.getMemberExpiryDate().isAfter(LocalDate.now());
        return isMember;
    }
}
