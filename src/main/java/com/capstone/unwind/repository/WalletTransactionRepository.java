package com.capstone.unwind.repository;

import com.capstone.unwind.entity.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {
    Optional<WalletTransaction> findById(UUID id);
    Page<WalletTransaction> findAllByWalletId(Integer walletId, Pageable pageable);
}