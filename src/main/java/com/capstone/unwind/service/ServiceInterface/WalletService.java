package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.entity.WalletTransaction;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.WalletDTO.WalletDto;
import com.capstone.unwind.model.WalletDTO.WalletRefereshDto;
import com.capstone.unwind.model.WalletDTO.WalletTransactionDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface WalletService {
    Page<WalletTransactionDto> getLoginCustomerWalletTransaction(Integer pageNo,Integer pageSize) throws OptionalNotFoundException;
    WalletRefereshDto getLoginCustomerWalletBalance() throws OptionalNotFoundException;
    WalletTransaction createTransactionVNPAY(float fee, float money, String paymentMethod) throws OptionalNotFoundException;
    WalletTransaction createTransactionWallet(float fee, float money, String paymentMethod);
    WalletTransaction updateTransaction(UUID uuid, String description, String transactionType) throws OptionalNotFoundException, ErrMessageException;
    WalletTransactionDto findWalletTransactionById(UUID uuid) throws OptionalNotFoundException;
    WalletTransaction updateTransactionMembershipByVNPAY(UUID uuid,Integer membership_id) throws OptionalNotFoundException, ErrMessageException;
    WalletTransaction updateTransactionDepositMoneyByVNPAY(UUID uuid) throws OptionalNotFoundException, ErrMessageException;
    WalletTransaction refundMoneyToCustomer(Integer customerId, float fee, float money, String paymentMethod, String description,String transactionType) throws OptionalNotFoundException;
    Page<WalletTransactionDto> getLoginCustomerMoneyReceivedTransactions(Integer pageNo,Integer pageSize) throws OptionalNotFoundException;
    Page<WalletTransactionDto> getLoginCustomerMoneySpentTransactions(Integer pageNo,Integer pageSize) throws OptionalNotFoundException;
}
