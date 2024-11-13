package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangeRequest;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExchangeRequestListMapper {
    @Mapping(source = "exchangePosting.roomInfoUnitTypePhotos", target = "exchangePosting.roomInfo.unitType.photos")
    @Mapping(source = "exchangePosting.roomInfoUnitTypeTitle", target = "exchangePosting.roomInfo.unitType.title")
    @Mapping(source = "exchangePosting.roomInfoUnitTypeId", target = "exchangePosting.roomInfo.unitType.id")
    @Mapping(source = "exchangePosting.roomInfoResortLogo", target = "exchangePosting.roomInfo.resort.logo")
    @Mapping(source = "exchangePosting.roomInfoResortResortName", target = "exchangePosting.roomInfo.resort.resortName")
    @Mapping(source = "exchangePosting.roomInfoResortId", target = "exchangePosting.roomInfo.resort.id")
    ExchangeRequest toEntity(ExchangeRequestBasicDto exchangeRequestBasicDto);

    @InheritInverseConfiguration(name = "toEntity")
    ExchangeRequestBasicDto toDto(ExchangeRequest exchangeRequest);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ExchangeRequest partialUpdate(ExchangeRequestBasicDto exchangeRequestBasicDto, @MappingTarget ExchangeRequest exchangeRequest);
}