package com.capstone.unwind.controller;

import com.capstone.unwind.entity.WalletTransaction;
import com.capstone.unwind.enums.WalletTransactionEnum;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.WalletDTO.MembershipResponseDto;
import com.capstone.unwind.model.WalletDTO.WalletDto;
import com.capstone.unwind.model.WalletDTO.WalletRefereshDto;
import com.capstone.unwind.model.WalletDTO.WalletTransactionDto;
import com.capstone.unwind.service.ServiceInterface.CustomerService;
import com.capstone.unwind.service.ServiceInterface.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@CrossOrigin
public class WalletController {
    @Autowired
    private final WalletService walletService;
    @Autowired
    private final CustomerService customerService;

    @GetMapping("/customer/wallet-transaction")
    private ResponseEntity<Page<WalletTransactionDto>> getCustomerWalletTransaction(@RequestParam(defaultValue = "0") int page,
                                                                                    @RequestParam(defaultValue = "10") int size) throws OptionalNotFoundException {
        Page<WalletTransactionDto> walletDtoPage = walletService.getLoginCustomerWalletTransaction(page,size);
        return ResponseEntity.ok(walletDtoPage);
    }
    @GetMapping("/customer/wallet-transaction/money-received")
    private ResponseEntity<Page<WalletTransactionDto>> getMoneyReceivedTransactions(@RequestParam(defaultValue = "0") int page,
                                                                                    @RequestParam(defaultValue = "10") int size) throws OptionalNotFoundException {
        Page<WalletTransactionDto> walletDtoPage = walletService.getLoginCustomerMoneyReceivedTransactions(page,size);
        return ResponseEntity.ok(walletDtoPage);
    }
    @GetMapping("/customer/wallet-transaction/money-spent")
    private ResponseEntity<Page<WalletTransactionDto>> getMoneySpentTransactions(@RequestParam(defaultValue = "0") int page,
                                                                                    @RequestParam(defaultValue = "10") int size) throws OptionalNotFoundException {
        Page<WalletTransactionDto> walletDtoPage = walletService.getLoginCustomerMoneySpentTransactions(page,size);
        return ResponseEntity.ok(walletDtoPage);
    }
    @GetMapping("/wallet-transaction/{uuid}")
    private ResponseEntity<WalletTransactionDto> getWalletTransactionById(@PathVariable UUID uuid) throws OptionalNotFoundException {
        WalletTransactionDto walletTransactionDto = walletService.findWalletTransactionById(uuid);
        return ResponseEntity.ok(walletTransactionDto);
    }
    @PostMapping("/vnpay/membership")
    public ResponseEntity<MembershipResponseDto> extendMembershipVNPAY(@RequestParam UUID uuid, @RequestParam Integer membership_id) throws ErrMessageException, OptionalNotFoundException {
        MembershipResponseDto membershipResponseDto = customerService.extendMembershipVNPAY(uuid,membership_id);
        return ResponseEntity.ok(membershipResponseDto);
    }
    @PostMapping("/wallet/membership")
    public ResponseEntity<MembershipResponseDto> extendMembershipWallet(@RequestParam Integer membership_id) throws ErrMessageException, OptionalNotFoundException {
        MembershipResponseDto membershipResponseDto = customerService.extendMembershipWallet(membership_id);
        return ResponseEntity.ok(membershipResponseDto);
    }
    @PostMapping("vnpay/rental/posting")
    public ResponseEntity<WalletTransactionDto> paymentRentalPostingVNPAY(@RequestParam UUID uuid,@RequestParam Integer rentalPackageId) throws ErrMessageException, OptionalNotFoundException {
        WalletTransactionDto walletTransactionDto = customerService.paymentRentalPostingVNPAY(uuid,rentalPackageId);
        return ResponseEntity.ok(walletTransactionDto);
    }
    @PostMapping("wallet/rental/posting")
    public ResponseEntity<WalletTransactionDto> paymentRentalPostingWallet(@RequestParam Integer rentalPackageId) throws ErrMessageException, OptionalNotFoundException {
        WalletTransactionDto walletTransactionDto = customerService.paymentRentalPostingWallet(rentalPackageId);
        return ResponseEntity.ok(walletTransactionDto);
    }
    @PostMapping("wallet/rental/booking")
    public ResponseEntity<WalletTransactionDto> paymentRentalBookingWallet(@RequestParam Integer postingId) throws ErrMessageException, OptionalNotFoundException {
        WalletTransactionDto walletTransactionDto = customerService.paymentRentalBookingWallet(postingId);
        return ResponseEntity.ok(walletTransactionDto);
    }
    @PostMapping("vnpay/rental/booking")
    public ResponseEntity<WalletTransactionDto> paymentRentalBookingVNPAY(@RequestParam UUID uuid,@RequestParam Integer postingId) throws ErrMessageException, OptionalNotFoundException {
        WalletTransactionDto walletTransactionDto = customerService.paymentRentalBookingVNPAY(uuid,postingId);
        return ResponseEntity.ok(walletTransactionDto);
    }
    @PostMapping("wallet/exchange/posting")
    public ResponseEntity<WalletTransactionDto> paymentExchangePostingWallet(@RequestParam Integer postingId) throws ErrMessageException, OptionalNotFoundException {
        WalletTransactionDto walletTransactionDto = customerService.paymentExchangePostingWallet(postingId);
        return ResponseEntity.ok(walletTransactionDto);
    }
    @PostMapping("vnpay/exchange/posting")
    public ResponseEntity<WalletTransactionDto> paymentExchangePostingVNPAY(@RequestParam UUID uuid,@RequestParam Integer postingId) throws ErrMessageException, OptionalNotFoundException {
        WalletTransactionDto walletTransactionDto = customerService.paymentExchangePostingVNPAY(uuid,postingId);
        return ResponseEntity.ok(walletTransactionDto);
    }
    @PostMapping("/vnpay/deposit-wallet")
    public ResponseEntity<WalletTransactionDto> depositMoneyToWalletVNPAY(@RequestParam UUID uuid) throws ErrMessageException, OptionalNotFoundException {
        WalletTransactionDto walletTransactionDto = customerService.depositMoneyVNPAY(uuid);
        return ResponseEntity.ok(walletTransactionDto);
    }
    @GetMapping("/customer")
    public ResponseEntity<WalletRefereshDto> getWalletCustomer() throws OptionalNotFoundException {
        WalletRefereshDto walletRefereshDto = walletService.getLoginCustomerWalletBalance();
        return ResponseEntity.ok(walletRefereshDto);
    }
    @GetMapping("/timeshare-company/wallet-transaction/money-received")
    private ResponseEntity<Page<WalletTransactionDto>> getMoneyTSReceivedTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws OptionalNotFoundException {
        Page<WalletTransactionDto> walletDtoPage = walletService.getTsCompanyMoneyReceivedTransactions(page,size);
        return ResponseEntity.ok(walletDtoPage);
    }
    @GetMapping("/system-staff/wallet-transaction")
    private ResponseEntity<Page<WalletTransactionDto>> getPaginationTransactionAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) WalletTransactionEnum walletTransactionEnum) {
        Page<WalletTransactionDto> walletDtoPage = walletService.getPaginationTransactionAdmin(page,size,walletTransactionEnum);
        return ResponseEntity.ok(walletDtoPage);
    }

}
