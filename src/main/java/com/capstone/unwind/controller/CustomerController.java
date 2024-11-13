package com.capstone.unwind.controller;

import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.CustomerDTO.*;
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

    @GetMapping("/initialize")
    public ResponseEntity<CustomerInitDto> getMyInitValue() throws OptionalNotFoundException {
        CustomerInitDto customerInitDto = customerService.getLoginCustomer();
        return ResponseEntity.ok(customerInitDto);
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> getProfile() throws OptionalNotFoundException {
            ProfileDto profile = customerService.getProfile();
            return ResponseEntity.ok(profile);
        }
    @GetMapping("/profile/{customerId}")
    public ResponseEntity<ProfileDto> getProfileById(@PathVariable Integer customerId) throws OptionalNotFoundException {
        ProfileDto profile = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(profile);
    }
    @PutMapping("/profile")
    public ResponseEntity<ProfileDto> updateProfile(@RequestBody UpdateProfileDto profileUpdateDto) throws OptionalNotFoundException, ErrMessageException {
            ProfileDto updatedProfile = customerService.updateProfile(profileUpdateDto);
            return ResponseEntity.ok(updatedProfile);
    }
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkCustomerExists(@RequestParam Integer userId) {
        boolean exists = customerService.checkCustomerExists(userId);
        return ResponseEntity.ok(exists);
    }
}
