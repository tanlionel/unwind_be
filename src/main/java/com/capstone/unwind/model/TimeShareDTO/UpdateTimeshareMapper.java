package com.capstone.unwind.model.TimeShareDTO;

import com.capstone.unwind.entity.Timeshare;
import com.capstone.unwind.model.TimeShareDTO.UpdateTimeshareResponseDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UpdateTimeshareMapper {
    Timeshare toEntity(UpdateTimeshareResponseDto updateTimeshareResponseDto);

    UpdateTimeshareResponseDto toDto(Timeshare timeshare);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Timeshare partialUpdate(UpdateTimeshareResponseDto updateTimeshareResponseDto, @MappingTarget Timeshare timeshare);
}