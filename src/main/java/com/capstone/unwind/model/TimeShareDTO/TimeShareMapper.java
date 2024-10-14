package com.capstone.unwind.model.TimeShareDTO;

import com.capstone.unwind.entity.Timeshare;
import com.capstone.unwind.entity.User;
import com.capstone.unwind.model.UserDTO.UserDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TimeShareMapper {

    @Mapping(source = "timeShareId", target = "id")
    @Mapping(source = "owner", target = "owner.fullName")
    @Mapping(source = "roomInfoId", target = "roomInfo.id")
    Timeshare toEntity(TimeShareDTO timeShareDTO);


    @Mapping(source = "id",target = "timeShareId")
    @Mapping(source = "owner.fullName", target = "owner")
    @Mapping(source = "roomInfo.id", target = "roomInfoId")
    TimeShareDTO toDto(Timeshare timeshare);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Timeshare partialUpdate(TimeShareDTO timeShareDTO, @MappingTarget Timeshare timeshare);
}
