package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.entity.WalletTransaction;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.WalletDTO.WalletDto;
import com.capstone.unwind.model.WalletDTO.WalletTransactionDto;

import java.util.UUID;

public interface WalletService {
    WalletDto getLoginCustomerWallet() throws OptionalNotFoundException;
    WalletTransaction createTransactionVNPAY(float fee, float money, String paymentMethod) throws OptionalNotFoundException;
    WalletTransaction createTransactionWallet(float fee, float money, String paymentMethod);
    WalletTransaction updateTransaction(UUID uuid, String description, String transactionType) throws OptionalNotFoundException, ErrMessageException;
    WalletTransactionDto findWalletTransactionById(UUID uuid) throws OptionalNotFoundException;
    WalletTransaction updateTransactionMembershipByVNPAY(UUID uuid,Integer membership_id) throws OptionalNotFoundException, ErrMessageException;
    WalletTransaction updateTransactionDepositMoneyByVNPAY(UUID uuid) throws OptionalNotFoundException, ErrMessageException;
}
