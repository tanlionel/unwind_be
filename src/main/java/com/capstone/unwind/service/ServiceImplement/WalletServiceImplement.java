package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.WalletDTO.*;
import com.capstone.unwind.repository.CustomerRepository;
import com.capstone.unwind.repository.MembershipRepository;
import com.capstone.unwind.repository.WalletRepository;
import com.capstone.unwind.repository.WalletTransactionRepository;
import com.capstone.unwind.service.ServiceInterface.UserService;
import com.capstone.unwind.service.ServiceInterface.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final WalletTransactionMapper walletTransactionMapper;
    @Autowired
    private final WalletRefreshMapper walletRefreshMapper;
    @Autowired
    private final WalletRepository walletRepository;

    @Override
    public Page<WalletTransactionDto> getLoginCustomerWalletTransaction(Integer pageNo,Integer pageSize) throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("createdAt").descending());
        Page<WalletTransaction> walletTransactionsPage = walletTransactionRepository.findAllByWalletId(user.getCustomer().getWallet().getId(),pageable);
        Page<WalletTransactionDto> walletTransactionDtoPage = walletTransactionsPage.map(walletTransactionMapper::toDto);
        return walletTransactionDtoPage;
    }

    @Override
    public Page<WalletTransactionDto> getLoginCustomerMoneyReceivedTransactions(Integer pageNo,Integer pageSize) throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("createdAt").descending());
        Page<WalletTransaction> walletTransactionsPage = walletTransactionRepository.findAllMoneyReceived(user.getCustomer().getWallet().getId(),pageable);
        Page<WalletTransactionDto> walletTransactionDtoPage = walletTransactionsPage.map(walletTransactionMapper::toDto);
        return walletTransactionDtoPage;
    }
    @Override
    public Page<WalletTransactionDto> getLoginCustomerMoneySpentTransactions(Integer pageNo,Integer pageSize) throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("createdAt").descending());
        Page<WalletTransaction> walletTransactionsPage = walletTransactionRepository.findAllMoneySpent(user.getCustomer().getWallet().getId(),pageable);
        Page<WalletTransactionDto> walletTransactionDtoPage = walletTransactionsPage.map(walletTransactionMapper::toDto);
        return walletTransactionDtoPage;
    }
    @Override
    public WalletRefereshDto getLoginCustomerWalletBalance() throws OptionalNotFoundException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        Wallet wallet = user.getCustomer().getWallet();
        WalletRefereshDto walletRefereshDto = walletRefreshMapper.toDto(wallet);
        return walletRefereshDto;
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
    public WalletTransaction createTransactionWallet(float fee, float money, String paymentMethod) {
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .fee(fee)
                .money(-money)
                .paymentMethod(paymentMethod)
                .build();
        WalletTransaction walletTransactionInDb = walletTransactionRepository.save(walletTransaction);
        return walletTransactionInDb;
    }

    @Override
    public WalletTransaction updateTransaction(UUID uuid, String description, String transactionType) throws OptionalNotFoundException, ErrMessageException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");
        Optional<WalletTransaction> walletTransaction = walletTransactionRepository.findById(uuid);
        if (!walletTransaction.isPresent()) throw new OptionalNotFoundException("not found transaction");
        WalletTransaction walletTransactionInDB;
        try{
            walletTransaction.get().setWallet(user.getCustomer().getWallet());
            walletTransaction.get().setDescription(description);
            walletTransaction.get().setTransactionType(transactionType);
            walletTransactionInDB = walletTransactionRepository.save(walletTransaction.get());
        }catch (Exception e){
            throw new ErrMessageException("error when save wallet");
        }

        return walletTransactionInDB;
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
        if (!walletTransaction.isPresent()) throw new OptionalNotFoundException("not found transaction");
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

    @Override
    public WalletTransaction updateTransactionDepositMoneyByVNPAY(UUID uuid) throws OptionalNotFoundException, ErrMessageException {
        User user = userService.getLoginUser();
        if (user.getCustomer() == null) throw new OptionalNotFoundException("Not init customer yet");
        if (user.getCustomer().getWallet()==null) throw new OptionalNotFoundException("Not init wallet yet");

        Optional<WalletTransaction> walletTransaction = walletTransactionRepository.findById(uuid);
        if (!walletTransaction.isPresent()) throw new OptionalNotFoundException("not found transaction");
        WalletTransaction walletTransactionInDB;
        try{
            walletTransaction.get().setWallet(user.getCustomer().getWallet());
            walletTransaction.get().setDescription("Nạp tiền vào ví");
            walletTransaction.get().setTransactionType("DEPOSITMONEY");
            walletTransaction.get().setMoney(-walletTransaction.get().getMoney());
            walletTransactionInDB = walletTransactionRepository.save(walletTransaction.get());
        }catch (Exception e){
            throw new ErrMessageException("error when save wallet");
        }

        return walletTransactionInDB;
    }

    @Override
    public WalletTransaction refundMoneyToCustomer(Integer customerId, float fee, float money, String paymentMethod, String description, String transactionType) throws OptionalNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->new OptionalNotFoundException("not found customer"));
        Wallet wallet = customer.getWallet();
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .fee(fee)
                .money(money)
                .description(description)
                .paymentMethod(paymentMethod)
                .transactionType(transactionType)
                .wallet(wallet)
                .build();
        wallet.setAvailableMoney(wallet.getAvailableMoney()+money);
        Wallet walletInDb = walletRepository.save(wallet);
        WalletTransaction walletTransactionInDb = walletTransactionRepository.save(walletTransaction);
        return walletTransactionInDb;
    }
}
