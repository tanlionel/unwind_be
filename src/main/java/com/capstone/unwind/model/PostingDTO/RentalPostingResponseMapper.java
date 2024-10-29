package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RentalPostingResponseMapper {
    @Mapping(source = "ownerId", target = "owner.id")
    @Mapping(source = "rentalPackageRentalPackageName", target = "rentalPackage.rentalPackageName")
    @Mapping(source = "rentalPackageId", target = "rentalPackage.id")
    @Mapping(source = "cancellationTypeName", target = "cancellationType.name")
    @Mapping(source = "cancellationTypeId", target = "cancellationType.id")
    @Mapping(source = "roomInfoId", target = "roomInfo.id")
    @Mapping(source = "timeshareId", target = "timeshare.id")
    RentalPosting toEntity(RentalPostingResponseDto rentalPostingResponseDto);

    @InheritInverseConfiguration(name = "toEntity")
    RentalPostingResponseDto toDto(RentalPosting rentalPosting);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RentalPosting partialUpdate(RentalPostingResponseDto rentalPostingResponseDto, @MappingTarget RentalPosting rentalPosting);
}