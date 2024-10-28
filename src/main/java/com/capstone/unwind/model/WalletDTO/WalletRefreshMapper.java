package com.capstone.unwind.model.WalletDTO;

import com.capstone.unwind.entity.Wallet;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface WalletRefreshMapper {
    @Mapping(source = "ownerId", target = "owner.id")
    Wallet toEntity(WalletRefereshDto walletDto);

    @Mapping(source = "owner.id", target = "ownerId")
    WalletRefereshDto toDto(Wallet wallet);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "ownerId", target = "owner.id")
    Wallet partialUpdate(WalletRefereshDto walletDto, @MappingTarget Wallet wallet);
}