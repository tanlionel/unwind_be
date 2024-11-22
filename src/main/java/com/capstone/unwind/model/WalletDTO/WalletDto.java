package com.capstone.unwind.model.WalletDTO;

import com.capstone.unwind.entity.Wallet;
import com.capstone.unwind.entity.WalletTransaction;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link Wallet}
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto implements Serializable {
    Integer id;
    Integer ownerId;
    Float availableMoney;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    Timestamp updatedAt;
    Boolean isActive;
    String type;
    List<WalletTransactionDto> transactions;

    /**
     * DTO for {@link WalletTransaction}
     */
    @Value
    public static class WalletTransactionDto implements Serializable {
        UUID id;
        Float money;
        String description;
        String paymentMethod;
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
        Timestamp createdAt;
    }
}