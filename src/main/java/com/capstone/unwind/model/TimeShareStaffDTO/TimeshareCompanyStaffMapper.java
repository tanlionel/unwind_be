package com.capstone.unwind.model.TimeShareStaffDTO;

import com.capstone.unwind.entity.TimeshareCompany;
import com.capstone.unwind.entity.TimeshareCompanyStaff;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TimeshareCompanyStaffMapper {

    @Mapping(source = "timeshareCompanyId", target = "timeshareCompany.id")
    TimeshareCompanyStaff toEntity(TimeShareCompanyStaffDTO timeShareCompanyStaffDTO);

    @InheritInverseConfiguration(name = "toEntity")
    @Mapping(source = "timeshareCompany.id", target = "timeshareCompanyId")
    @Mapping(source = "resort.id", target = "resortId")
    @Mapping(source = "username",target = "userName")
    TimeShareCompanyStaffDTO toDto(TimeshareCompanyStaff timeshareCompanyStaff);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "timeshareCompanyId", target = "timeshareCompany.id")
    @Mapping(source = "resortId", target = "resort.id")
    TimeshareCompanyStaff partialUpdate(TimeShareCompanyStaffDTO timeShareCompanyStaffDTO, @MappingTarget TimeshareCompanyStaff timeshareCompanyStaff);
}
