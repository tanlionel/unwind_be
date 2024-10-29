package com.capstone.unwind.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "wallet_transaction")
public class WalletTransaction {

    @Id
    @Column(name = "wallet_transaction_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "money")
    private Float money;

    @Column(name = "transaction_type", length = 45)
    private String transactionType;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "payment_method" , length = 100)
    private String paymentMethod;

    @Column(name = "created_at")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp createdAt;

    @Column(name = "fee")
    private Float fee;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    
}
