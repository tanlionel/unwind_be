package com.capstone.unwind.controller;

import com.capstone.unwind.entity.WalletTransaction;
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
    @GetMapping("/wallet-transaction/{uuid}")
    private ResponseEntity<WalletTransactionDto> getWalletTransactionById(@PathVariable UUID uuid) throws OptionalNotFoundException {
        WalletTransactionDto walletTransactionDto = walletService.findWalletTransactionById(uuid);
        return ResponseEntity.ok(walletTransactionDto);
    }
    @PostMapping("/VNPAY/membership")
    public ResponseEntity<MembershipResponseDto> extendMembershipVNPAY(@RequestParam UUID uuid, @RequestParam Integer membership_id) throws ErrMessageException, OptionalNotFoundException {
        MembershipResponseDto membershipResponseDto = customerService.extendMembershipVNPAY(uuid,membership_id);
        return ResponseEntity.ok(membershipResponseDto);
    }
    @PostMapping("/wallet/membership")
    public ResponseEntity<MembershipResponseDto> extendMembershipWallet(@RequestParam Integer membership_id) throws ErrMessageException, OptionalNotFoundException {
        MembershipResponseDto membershipResponseDto = customerService.extendMembershipWallet(membership_id);
        return ResponseEntity.ok(membershipResponseDto);
    }
    @PostMapping("VNPAY/rental/posting")
    public ResponseEntity<WalletTransactionDto> paymentRentalPostingVNPAY(@RequestParam UUID uuid,@RequestParam Integer rentalPackageId) throws ErrMessageException, OptionalNotFoundException {
        WalletTransactionDto walletTransactionDto = customerService.paymentRentalPostingVNPAY(uuid,rentalPackageId);
        return ResponseEntity.ok(walletTransactionDto);
    }
    @PostMapping("WALLET/rental/posting")
    public ResponseEntity<WalletTransactionDto> paymentRentalPostingWallet(@RequestParam Integer rentalPackageId) throws ErrMessageException, OptionalNotFoundException {
        WalletTransactionDto walletTransactionDto = customerService.paymentRentalPostingWallet(rentalPackageId);
        return ResponseEntity.ok(walletTransactionDto);
    }
    @PostMapping("/VNPAY/deposit-wallet")
    public ResponseEntity<WalletTransactionDto> depositMoneyToWalletVNPAY(@RequestParam UUID uuid) throws ErrMessageException, OptionalNotFoundException {
        WalletTransactionDto walletTransactionDto = customerService.depositMoneyVNPAY(uuid);
        return ResponseEntity.ok(walletTransactionDto);
    }
    @GetMapping("/customer")
    public ResponseEntity<WalletRefereshDto> getWalletCustomer() throws OptionalNotFoundException {
        WalletRefereshDto walletRefereshDto = walletService.getLoginCustomerWalletBalance();
        return ResponseEntity.ok(walletRefereshDto);
    }

}
