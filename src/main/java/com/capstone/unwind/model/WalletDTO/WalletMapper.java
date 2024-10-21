package com.capstone.unwind.model.WalletDTO;

import com.capstone.unwind.entity.Wallet;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface WalletMapper {
    @Mapping(source = "ownerId", target = "owner.id")
    Wallet toEntity(WalletDto walletDto);

    @AfterMapping
    default void linkTransactions(@MappingTarget Wallet wallet) {
        wallet.getTransactions().forEach(transaction -> transaction.setWallet(wallet));
    }

    @Mapping(source = "owner.id", target = "ownerId")
    WalletDto toDto(Wallet wallet);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "ownerId", target = "owner.id")
    Wallet partialUpdate(WalletDto walletDto, @MappingTarget Wallet wallet);
}