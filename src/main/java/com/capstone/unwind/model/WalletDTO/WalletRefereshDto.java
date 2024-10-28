package com.capstone.unwind.model.WalletDTO;

import com.capstone.unwind.entity.Wallet;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Wallet}
 */
@Value
public class WalletRefereshDto implements Serializable {
    Integer id;
    Integer ownerId;
    Float availableMoney;
}