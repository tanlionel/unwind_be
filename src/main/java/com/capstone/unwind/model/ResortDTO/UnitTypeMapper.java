package com.capstone.unwind.model.ResortDTO;

import com.capstone.unwind.entity.UnitType;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UnitTypeMapper {
    @Mapping(source = "resortId", target = "resort.id")
    UnitType toEntity(UnitTypeDto unitTypeDto);

    @Mapping(source = "resort.id", target = "resortId")
    UnitTypeDto toDto(UnitType unitType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "resortId", target = "resort.id")
    UnitType partialUpdate(UnitTypeDto unitTypeDto, @MappingTarget UnitType unitType);
}