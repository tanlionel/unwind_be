package com.capstone.unwind.model.BookingDTO;

import com.capstone.unwind.entity.ExchangeBooking;
import com.capstone.unwind.model.BookingDTO.ExchangeBookingDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExchangeBookingMapper {
    ExchangeBooking toEntity(ExchangeBookingDto exchangeBookingDto);

    ExchangeBookingDto toDto(ExchangeBooking exchangeBooking);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ExchangeBooking partialUpdate(ExchangeBookingDto exchangeBookingDto, @MappingTarget ExchangeBooking exchangeBooking);
}