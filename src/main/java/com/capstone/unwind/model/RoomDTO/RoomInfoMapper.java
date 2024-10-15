package com.capstone.unwind.model.RoomDTO;

import com.capstone.unwind.entity.RoomInfo;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomInfoMapper {
    @Mapping(source = "unitTypeId", target = "unitType.id")
    @Mapping(source = "resortId", target = "resort.id")
    RoomInfo toEntity(RoomInfoDto roomInfoDto);

    @InheritInverseConfiguration(name = "toEntity")
    RoomInfoDto toDto(RoomInfo roomInfo);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoomInfo partialUpdate(RoomInfoDto roomInfoDto, @MappingTarget RoomInfo roomInfo);
}