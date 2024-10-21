package com.capstone.unwind.model.WalletDTO;

import com.capstone.unwind.entity.WalletTransaction;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface WalletTransactionMapper {
    @Mapping(source = "walletId", target = "wallet.id")
    WalletTransaction toEntity(WalletTransactionDto walletTransactionDto);

    @Mapping(source = "wallet.id", target = "walletId")
    WalletTransactionDto toDto(WalletTransaction walletTransaction);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "walletId", target = "wallet.id")
    WalletTransaction partialUpdate(WalletTransactionDto walletTransactionDto, @MappingTarget WalletTransaction walletTransaction);
}