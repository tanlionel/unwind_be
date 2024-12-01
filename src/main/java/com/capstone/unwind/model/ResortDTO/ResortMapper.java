package com.capstone.unwind.model.ResortDTO;

import com.capstone.unwind.entity.Resort;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResortMapper {
    @Mapping(source = "timeshareCompanyId", target = "timeshareCompany.id")
    @Mapping(source = "resortLocationName", target = "location.name")
    @Mapping(source = "resortLocationDisplayName", target = "location.displayName")
    Resort toEntity(ResortDto resortDto);

    @Mapping(source = "timeshareCompany.id", target = "timeshareCompanyId")
    @Mapping(source = "location.name",target ="resortLocationName" )
    @Mapping(source = "location.displayName",target ="resortLocationDisplayName" )
    ResortDto toDto(Resort resort);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "timeshareCompanyId", target = "timeshareCompany.id")
    Resort partialUpdate(ResortDto resortDto, @MappingTarget Resort resort);
}