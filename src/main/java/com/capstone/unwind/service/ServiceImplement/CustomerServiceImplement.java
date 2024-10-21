package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.Customer;
import com.capstone.unwind.entity.User;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.CustomerDTO.CustomerDto;
import com.capstone.unwind.model.CustomerDTO.CustomerMapper;
import com.capstone.unwind.model.CustomerDTO.CustomerRequestDto;
import com.capstone.unwind.repository.CustomerRepository;
import com.capstone.unwind.repository.UserRepository;
import com.capstone.unwind.service.ServiceInterface.CustomerService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImplement implements CustomerService {
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final CustomerMapper customerMapper;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserService userService;
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

    public boolean checkIsMember(Customer customer){
        boolean isMember = customer.getMemberExpiryDate()!=null && customer.getMemberExpiryDate().isAfter(LocalDate.now());
        return isMember;
    }
}
