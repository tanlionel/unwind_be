package com.capstone.unwind.model.BookingDTO;

import com.capstone.unwind.entity.RentalBooking;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RentalBookingMapper {
    RentalBooking toEntity(RentalBookingDto rentalBookingDto);

    @InheritInverseConfiguration(name = "toEntity")
    RentalBookingDto toDto(RentalBooking rentalBooking);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RentalBooking partialUpdate(RentalBookingDto rentalBookingDto, @MappingTarget RentalBooking rentalBooking);
}