package com.capstone.unwind.model.ResortDTO;

import com.capstone.unwind.entity.UnitType;
import com.capstone.unwind.entity.UnitTypeAmenity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UnitTypeAmentitiesMapper {
    @Mapping(source = "unitTypeId", target = "unitType.id")
    UnitTypeAmenity toEntity(UnitTypeAmenitiesDTO unitTypeAmenitiesDTO);

    @Mapping(source = "unitType.id", target = "unitTypeId")
    UnitTypeAmenitiesDTO toDto(UnitTypeAmenity unitTypeAmenity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "unitTypeId", target = "unitType.id")
    UnitTypeAmenity partialUpdate(UnitTypeAmenitiesDTO unitTypeAmenitiesDTO, @MappingTarget UnitTypeAmenity unitTypeAmenity);
}