package com.capstone.unwind.model.RoomDTO;

import com.capstone.unwind.entity.RoomAmenity;
import com.capstone.unwind.model.RoomDTO.RoomAmenityDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomAmenityMapper {
    RoomAmenity toEntity(RoomAmenityDto roomAmenityDto);

    RoomAmenityDto toDto(RoomAmenity roomAmenity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoomAmenity partialUpdate(RoomAmenityDto roomAmenityDto, @MappingTarget RoomAmenity roomAmenity);
}