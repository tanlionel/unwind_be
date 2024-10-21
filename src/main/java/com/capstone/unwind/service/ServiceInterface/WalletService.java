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
    WalletTransactionDto findWalletTransactionById(UUID uuid) throws OptionalNotFoundException;
    WalletTransaction updateTransactionMembershipByVNPAY(UUID uuid,Integer membership_id) throws OptionalNotFoundException, ErrMessageException;
}
