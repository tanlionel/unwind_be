package com.capstone.unwind.model.BookingDTO;

import com.capstone.unwind.entity.RentalBooking;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RentalBookingDetailMapper {
    @Mapping(source = "rentalPosting.rentalPackagePrice", target = "rentalPosting.rentalPackage.price")
    @Mapping(source = "rentalPosting.rentalPackageType", target = "rentalPosting.rentalPackage.type")
    @Mapping(source = "rentalPosting.rentalPackageRentalPackageName", target = "rentalPosting.rentalPackage.rentalPackageName")
    @Mapping(source = "rentalPosting.rentalPackageId", target = "rentalPosting.rentalPackage.id")
    @Mapping(source = "rentalPosting.roomInfo.unitType.resortId",target = "rentalPosting.roomInfo.unitType.resort.id")
    @Mapping(source = "rentalPosting.roomInfo.unitType.resortResortName",target = "rentalPosting.roomInfo.unitType.resort.resortName")
    @Mapping(source = "rentalPosting.roomInfo.unitType.resortLogo",target = "rentalPosting.roomInfo.unitType.resort.logo")
    @Mapping(source = "rentalPosting.roomInfo.unitType.resortLocationName",target = "rentalPosting.roomInfo.unitType.resort.location.name")
    @Mapping(source = "rentalPosting.roomInfo.unitType.resortLocationDisplayName",target = "rentalPosting.roomInfo.unitType.resort.location.displayName")
    @Mapping(source = "rentalPosting.roomInfo.unitType.resortDescription",target = "rentalPosting.roomInfo.unitType.resort.description")
    RentalBooking toEntity(RentalBookingDetailDto rentalBookingDto);

    @InheritInverseConfiguration(name = "toEntity")
    RentalBookingDetailDto toDto(RentalBooking rentalBooking);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RentalBooking partialUpdate(RentalBookingDetailDto rentalBookingDto, @MappingTarget RentalBooking rentalBooking);
}