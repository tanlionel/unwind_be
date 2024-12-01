package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangeRequest;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExchangeRequestPostingListMapper {
    @Mapping(source = "ownerAvatar", target = "owner.avatar")
    @Mapping(source = "ownerFullName", target = "owner.fullName")
    @Mapping(source = "ownerId", target = "owner.id")
    @Mapping(source = "roomInfo.resortId",target = "roomInfo.resort.id")
    @Mapping(source = "roomInfo.resortResortName",target = "roomInfo.resort.resortName")
    @Mapping(source = "roomInfo.resortLogo",target = "roomInfo.resort.logo")
    @Mapping(source = "roomInfo.resortLocationName",target = "roomInfo.resort.location.name")
    @Mapping(source = "roomInfo.resortLocationDisplayName",target = "roomInfo.resort.location.displayName")
    @Mapping(source = "roomInfo.resortDescription",target = "roomInfo.resort.description")
    @Mapping(source = "roomInfo.status",target = "roomInfo.resort.status")
    @Mapping(source = "roomInfo.unitTypeId",target = "roomInfo.unitType.id")
    @Mapping(source = "roomInfo.unitTypeTitle",target = "roomInfo.unitType.title")
    @Mapping(source = "roomInfo.unitTypePhotos",target = "roomInfo.unitType.photos")
    ExchangeRequest toEntity(ExchangeRequestPostingBasicDto exchangeRequestPostingBasicDto);

    @InheritInverseConfiguration(name = "toEntity")
    ExchangeRequestPostingBasicDto toDto(ExchangeRequest exchangeRequest);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ExchangeRequest partialUpdate(ExchangeRequestPostingBasicDto exchangeRequestPostingBasicDto, @MappingTarget ExchangeRequest exchangeRequest);
}