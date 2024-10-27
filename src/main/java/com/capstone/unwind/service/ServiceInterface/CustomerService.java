package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.CustomerDTO.CustomerDto;
import com.capstone.unwind.model.CustomerDTO.CustomerRequestDto;
import com.capstone.unwind.model.WalletDTO.MembershipResponseDto;
import com.capstone.unwind.model.WalletDTO.WalletDto;
import com.capstone.unwind.model.WalletDTO.WalletTransactionDto;

import java.util.UUID;

public interface CustomerService {
    CustomerDto createCustomer(CustomerRequestDto customerRequestDto) throws OptionalNotFoundException;
    CustomerDto getCustomerByUserId(Integer userId) throws OptionalNotFoundException;
    CustomerDto getCustomerByCustomerId(Integer customerId) throws OptionalNotFoundException;
    MembershipResponseDto extendMembershipVNPAY(UUID uuid, Integer membership_id) throws OptionalNotFoundException, ErrMessageException;
    WalletTransactionDto depositMoneyVNPAY(UUID uuid) throws OptionalNotFoundException, ErrMessageException;

    MembershipResponseDto extendMembershipWallet(Integer membershipId) throws OptionalNotFoundException, ErrMessageException;
}
