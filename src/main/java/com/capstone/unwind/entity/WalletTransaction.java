package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "wallet_transaction")
public class WalletTransaction {
    @Id
    @Column(name = "wallet_transaction_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "money")
    private Float money;

    @Column(name = "transaction_type", length = 45)
    private String transactionType;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "created_at")
    private Timestamp createdAt;

}