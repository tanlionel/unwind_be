package com.capstone.unwind.model.ResortDTO;

import com.capstone.unwind.entity.UnitType;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UnitTypesMapper {
    @Mapping(source = "resortId", target = "resort.id")
    UnitType toEntity(UnitTypeResponseDTO unitTypeResponseDTO);

    @Mapping(source = "resort.id", target = "resortId")
    UnitTypeResponseDTO toDto(UnitType unitType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "resortId", target = "resort.id")
    UnitType partialUpdate(UnitTypeResponseDTO unitTypeResponseDTO, @MappingTarget UnitType unitType);
}