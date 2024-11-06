package com.capstone.unwind.model.BookingDTO;

import com.capstone.unwind.entity.ExchangeBooking;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExchangeBookingDetailMapper {

    ExchangeBooking toEntity(ExchangeBookingDetailDto exchangeBookingDetailDto);
    @Mapping(source = "exchangePosting.roomInfo.unitType.resort.id",target = "roomInfo.unitType.resortId")
    @Mapping(source = "exchangePosting.roomInfo.unitType.resort.resortName",target = "roomInfo.unitType.resortName")
    @Mapping(source = "exchangePosting.roomInfo.unitType.resort.logo",target = "roomInfo.unitType.resortLogo")
    @Mapping(source = "exchangePosting.roomInfo.unitType.resort.description",target = "roomInfo.unitType.resortDescription")
    @Mapping(source = "exchangePosting.roomInfo.unitType.title",target = "roomInfo.unitType.title")
    @Mapping(source = "exchangePosting.roomInfo.unitType.price",target = "roomInfo.unitType.price")
    @Mapping(source = "exchangePosting.roomInfo.unitType.description",target = "roomInfo.unitType.description")
    @Mapping(source = "exchangePosting.roomInfo.unitType.photos",target = "roomInfo.unitType.photos")
    @Mapping(source = "exchangePosting.roomInfo.roomInfoCode",target = "roomInfo.roomInfoCode")
    @Mapping(source = "exchangePosting.roomInfo.roomInfoName",target = "roomInfo.roomInfoName")
    ExchangeBookingDetailDto toDto(ExchangeBooking exchangeBooking);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ExchangeBooking partialUpdate(ExchangeBookingDetailDto exchangeBookingDetailDto, @MappingTarget ExchangeBooking exchangeBooking);
}