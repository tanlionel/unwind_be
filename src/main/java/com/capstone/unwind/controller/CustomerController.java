package com.capstone.unwind.controller;

import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.CustomerDTO.CustomerDto;
import com.capstone.unwind.model.CustomerDTO.CustomerRequestDto;
import com.capstone.unwind.service.ServiceInterface.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin
public class CustomerController {
    @Autowired
    private final CustomerService customerService;

    @GetMapping("{customerId}")
    public ResponseEntity<CustomerDto> getCustomerByCustomerId(@PathVariable Integer customerId) throws OptionalNotFoundException {
        CustomerDto customerDto = customerService.getCustomerByCustomerId(customerId);
        return ResponseEntity.ok(customerDto);
    }
    @GetMapping("user/{userId}")
    public ResponseEntity<CustomerDto> getCustomerByUserId(@PathVariable Integer userId) throws OptionalNotFoundException {
        CustomerDto customerDto = customerService.getCustomerByUserId(userId);
        return ResponseEntity.ok(customerDto);
    }
    @PostMapping()
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerRequestDto customerRequestDto) throws OptionalNotFoundException {
        CustomerDto customerDto = customerService.createCustomer(customerRequestDto);
        return ResponseEntity.ok(customerDto);
    }
}
