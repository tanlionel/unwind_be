package com.capstone.unwind.model.ResortDTO;

import com.capstone.unwind.entity.Resort;
import com.capstone.unwind.model.ResortDTO.ResortRandomDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResortRandomMapper {
    Resort toEntity(ResortRandomDto resortRandomDto);

    ResortRandomDto toDto(Resort resort);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Resort partialUpdate(ResortRandomDto resortRandomDto, @MappingTarget Resort resort);
}