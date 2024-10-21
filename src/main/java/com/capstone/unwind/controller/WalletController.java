package com.capstone.unwind.controller;

import com.capstone.unwind.entity.WalletTransaction;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.WalletDTO.WalletDto;
import com.capstone.unwind.model.WalletDTO.WalletTransactionDto;
import com.capstone.unwind.service.ServiceInterface.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/customer")
    private ResponseEntity<WalletDto> getCustomerWallet() throws OptionalNotFoundException {
        WalletDto walletDto = walletService.getLoginCustomerWallet();
        return ResponseEntity.ok(walletDto);
    }
    @GetMapping("/wallet-transaction/{uuid}")
    private ResponseEntity<WalletTransactionDto> getWalletTransactionById(@PathVariable UUID uuid) throws OptionalNotFoundException {
        WalletTransactionDto walletTransactionDto = walletService.findWalletTransactionById(uuid);
        return ResponseEntity.ok(walletTransactionDto);
    }
}
