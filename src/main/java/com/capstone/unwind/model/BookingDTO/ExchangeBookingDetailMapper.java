package com.capstone.unwind.model.BookingDTO;

import com.capstone.unwind.entity.ExchangeBooking;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExchangeBookingDetailMapper {

    ExchangeBooking toEntity(ExchangeBookingDetailDto exchangeBookingDetailDto);
    @Mapping(source = "roomInfo.unitType.resort.id", target = "roomInfo.unitType.resortId")
    @Mapping(source = "roomInfo.unitType.resort.resortName", target = "roomInfo.unitType.resortName")
    @Mapping(source = "roomInfo.unitType.resort.logo", target = "roomInfo.resortLogo")
    @Mapping(source = "roomInfo.unitType.resort.description", target = "roomInfo.unitType.resortDescription")
    @Mapping(source = "roomInfo.unitType.resort.location", target = "roomInfo.unitType.location")
    @Mapping(source = "roomInfo.unitType.title", target = "roomInfo.unitType.title")
    @Mapping(source = "roomInfo.unitType.price", target = "roomInfo.unitType.price")
    @Mapping(source = "roomInfo.unitType.description", target = "roomInfo.unitType.description")
    @Mapping(source = "roomInfo.unitType.photos", target = "roomInfo.unitType.photos")
    @Mapping(source = "roomInfo.roomInfoCode", target = "roomInfo.roomInfoCode")
    @Mapping(source = "roomInfo.roomInfoName", target = "roomInfo.roomInfoName")
    ExchangeBookingDetailDto toDto(ExchangeBooking exchangeBooking);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ExchangeBooking partialUpdate(ExchangeBookingDetailDto exchangeBookingDetailDto, @MappingTarget ExchangeBooking exchangeBooking);
}