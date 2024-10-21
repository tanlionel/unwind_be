package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.CustomerDTO.CustomerDto;
import com.capstone.unwind.model.CustomerDTO.CustomerRequestDto;

public interface CustomerService {
    CustomerDto createCustomer(CustomerRequestDto customerRequestDto) throws OptionalNotFoundException;
    CustomerDto getCustomerByUserId(Integer userId) throws OptionalNotFoundException;
    CustomerDto getCustomerByCustomerId(Integer customerId) throws OptionalNotFoundException;
}
