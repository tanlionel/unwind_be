package com.capstone.unwind.model.TimeshareCompany;

import com.capstone.unwind.entity.TimeshareCompany;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TimeshareCompanyMapper {
    @Mapping(source = "ownerId", target = "owner.id")
    TimeshareCompany toEntity(TimeshareCompanyDto timeshareCompanyDto);

    @InheritInverseConfiguration(name = "toEntity")
    @Mapping(source = "owner.id", target = "ownerId")
    TimeshareCompanyDto toDto(TimeshareCompany timeshareCompany);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "ownerId", target = "owner.id")
    TimeshareCompany partialUpdate(TimeshareCompanyDto timeshareCompanyDto, @MappingTarget TimeshareCompany timeshareCompany);
}