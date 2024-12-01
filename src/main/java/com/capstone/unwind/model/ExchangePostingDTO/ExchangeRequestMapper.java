package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangeRequest;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExchangeRequestMapper {
    @Mapping(source = "exchangePosting.roomInfoUnitTypePhotos", target = "exchangePosting.roomInfo.unitType.photos")
    @Mapping(source = "exchangePosting.roomInfoUnitTypePrice", target = "exchangePosting.roomInfo.unitType.price")
    @Mapping(source = "exchangePosting.roomInfoUnitTypeTitle", target = "exchangePosting.roomInfo.unitType.title")
    @Mapping(source = "exchangePosting.roomInfoUnitTypeId", target = "exchangePosting.roomInfo.unitType.id")
    @Mapping(source = "exchangePosting.roomInfoResortLogo", target = "exchangePosting.roomInfo.resort.logo")
    @Mapping(source = "exchangePosting.roomInfoResortResortName", target = "exchangePosting.roomInfo.resort.resortName")
    @Mapping(source = "exchangePosting.roomInfoResortId", target = "exchangePosting.roomInfo.resort.id")
    @Mapping(source = "exchangePosting.roomInfoRoomInfoCode", target = "exchangePosting.roomInfo.roomInfoCode")
    @Mapping(source = "exchangePosting.roomInfoId", target = "exchangePosting.roomInfo.id")
    @Mapping(source = "roomInfo.resortId", target = "roomInfo.resort.id")
    @Mapping(source = "roomInfo.resortResortName", target = "roomInfo.resort.resortName")
    @Mapping(source = "roomInfo.resortLogo", target = "roomInfo.resort.logo")
    @Mapping(source = "roomInfo.resortLocationName", target = "roomInfo.resort.location.name")
    @Mapping(source = "roomInfo.resortLocationDisplayName", target = "roomInfo.resort.location.displayName")
    @Mapping(source = "roomInfo.resortDescription", target = "roomInfo.resort.description")
    @Mapping(source = "ownerAvatar", target = "owner.avatar")
    @Mapping(source = "ownerFullName", target = "owner.fullName")
    @Mapping(source = "ownerId", target = "owner.id")
    ExchangeRequest toEntity(ExchangeRequestDetailDto exchangeRequestDetailDto);

    @InheritInverseConfiguration(name = "toEntity")
    ExchangeRequestDetailDto toDto(ExchangeRequest exchangeRequest);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ExchangeRequest partialUpdate(ExchangeRequestDetailDto exchangeRequestDetailDto, @MappingTarget ExchangeRequest exchangeRequest);
}