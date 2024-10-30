package com.capstone.unwind.model.PostingDTO;

import com.capstone.unwind.entity.RentalPosting;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RentalPostingApprovalMapper {
    @Mapping(source = "ownerId", target = "owner.id")
    @Mapping(source = "rentalPackageRentalPackageName", target = "rentalPackage.rentalPackageName")
    @Mapping(source = "rentalPackageId", target = "rentalPackage.id")
    @Mapping(source = "roomInfoId", target = "roomInfo.id")
    @Mapping(source = "timeshareId", target = "timeshare.id")
    RentalPosting toEntity(RentalPostingApprovalResponseDto rentalPostingApprovalResponseDto);

    @InheritInverseConfiguration(name = "toEntity")
    RentalPostingApprovalResponseDto toDto(RentalPosting rentalPosting);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RentalPosting partialUpdate(RentalPostingApprovalResponseDto rentalPostingApprovalResponseDto, @MappingTarget RentalPosting rentalPosting);
}