package com.capstone.unwind.model.ResortDTO;

import com.capstone.unwind.entity.Resort;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResortMapper {
    @Mapping(source = "timeshareCompanyId", target = "timeshareCompany.id")
    @Mapping(source = "resortLocationName", target = "location.name")
    @Mapping(source = "resortLocationDisplayName", target = "location.displayName")
    @Mapping(source = "resortLocationLatitude", target = "location.latitude")
    @Mapping(source = "resortLocationLongitude", target = "location.longitude")
    Resort toEntity(ResortDto resortDto);

    @Mapping(source = "timeshareCompany.id", target = "timeshareCompanyId")
    @Mapping(source = "location.name",target ="resortLocationName" )
    @Mapping(source = "location.displayName",target ="resortLocationDisplayName" )
    @Mapping(source = "location.latitude",target ="resortLocationLatitude" )
    @Mapping(source = "location.longitude",target ="resortLocationLongitude" )
    ResortDto toDto(Resort resort);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "timeshareCompanyId", target = "timeshareCompany.id")
    Resort partialUpdate(ResortDto resortDto, @MappingTarget Resort resort);
}