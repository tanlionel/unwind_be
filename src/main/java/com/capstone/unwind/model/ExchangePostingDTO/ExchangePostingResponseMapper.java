package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangePosting;
import com.capstone.unwind.entity.RentalPosting;
import com.capstone.unwind.model.PostingDTO.RentalPostingResponseDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExchangePostingResponseMapper {
    @Mapping(source = "ownerId", target = "owner.id")
    @Mapping(source = "exchangePackagePackageName", target = "exchangePackage.packageName")
    @Mapping(source = "exchangePackageId", target = "exchangePackage.id")
    @Mapping(source = "roomInfoId", target = "roomInfo.id")
    @Mapping(source = "timeshareId", target = "timeshare.id")
    ExchangePosting toEntity(ExchangePostingResponseDto exchangePostingResponseDto);

    @InheritInverseConfiguration(name = "toEntity")
    ExchangePostingResponseDto toDto(ExchangePosting exchangePosting);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ExchangePosting partialUpdate(ExchangePostingResponseDto exchangePostingResponseDto, @MappingTarget ExchangePosting exchangePosting);
}