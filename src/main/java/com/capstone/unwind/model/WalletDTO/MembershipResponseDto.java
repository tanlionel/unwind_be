package com.capstone.unwind.model.WalletDTO;

import com.capstone.unwind.entity.WalletTransaction;
import lombok.*;

@Data
@Getter
@Setter
@Builder
public class MembershipResponseDto {
    Integer customerId;
    WalletTransactionDto walletTransactionDto;
}
