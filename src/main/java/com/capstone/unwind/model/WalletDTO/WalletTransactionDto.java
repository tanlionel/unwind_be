package com.capstone.unwind.model.WalletDTO;

import com.capstone.unwind.entity.WalletTransaction;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * DTO for {@link WalletTransaction}
 */
@Value
public class WalletTransactionDto implements Serializable {
    UUID id;
    Integer walletId;
    Float money;
    String transactionType;
    String description;
    String paymentMethod;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss" , timezone = "Asia/Bangkok")
    Timestamp createdAt;
    Float fee;
}