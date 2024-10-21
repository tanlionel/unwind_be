package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.Membership;
import com.capstone.unwind.entity.User;
import com.capstone.unwind.entity.Wallet;
import com.capstone.unwind.entity.WalletTransaction;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.WalletDTO.WalletDto;
import com.capstone.unwind.model.WalletDTO.WalletMapper;
import com.capstone.unwind.model.WalletDTO.WalletTransactionDto;
import com.capstone.unwind.model.WalletDTO.WalletTransactionMapper;
import com.capstone.unwind.repository.CustomerRepository;
import com.capstone.unwind.repository.MembershipRepository;
import com.capstone.unwind.repository.WalletTransactionRepository;
import com.capstone.unwind.service.ServiceInterface.UserService;
import com.capstone.unwind.service.ServiceInterface.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImplement implements WalletService {
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final WalletMapper walletMapper;
    @Autowired
    private final WalletTransactionRepository walletTransactionRepository;
    @Autowired
    private final MembershipRepository membershipRepository;
    @Autowired
    private WalletTransactionMapper walletTransactionMapper;

    @Override
    public WalletDto getLoginCustomerWallet() throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        Wallet wallet = user.getCustomer().getWallet();
        return walletMapper.toDto(wallet);
    }

    @Override
    public WalletTransaction createTransactionVNPAY(float fee, float money, String paymentMethod) throws OptionalNotFoundException {
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .fee(fee)
                .money(-money)
                .paymentMethod(paymentMethod)
                .build();
        WalletTransaction walletTransactionInDb = walletTransactionRepository.save(walletTransaction);
        return walletTransactionInDb;
    }

    @Override
    public WalletTransactionDto findWalletTransactionById(UUID uuid) throws OptionalNotFoundException {
        Optional<WalletTransaction> walletTransaction = walletTransactionRepository.findById(uuid);
        if (!walletTransaction.isPresent()) throw new OptionalNotFoundException("not found");
        return walletTransactionMapper.toDto(walletTransaction.get());
    }

    @Override
    public WalletTransaction updateTransactionMembershipByVNPAY(UUID uuid,Integer membership_id) throws OptionalNotFoundException, ErrMessageException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");

        Optional<WalletTransaction> walletTransaction = walletTransactionRepository.findById(uuid);
        if (!walletTransaction.isPresent()) throw new OptionalNotFoundException("not found");
        WalletTransaction walletTransactionInDB;
        try{
        walletTransaction.get().setWallet(user.getCustomer().getWallet());
        Optional<Membership> membership = membershipRepository.findById(membership_id);
        if (!membership.isPresent()) throw new OptionalNotFoundException("not found membership package");
        walletTransaction.get().setDescription("Thanh toán membership "+membership.get().getDuration()+" tháng");
        walletTransaction.get().setTransactionType("MEMBERSHIP");
        walletTransactionInDB = walletTransactionRepository.save(walletTransaction.get());
        }catch (Exception e){
            throw new ErrMessageException("error when save wallet");
        }

        return walletTransactionInDB;
    }
}
