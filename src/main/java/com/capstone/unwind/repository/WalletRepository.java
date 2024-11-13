package com.capstone.unwind.repository;

import com.capstone.unwind.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Wallet findWalletByOwnerId(Integer ownerId);
}