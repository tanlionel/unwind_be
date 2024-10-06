package com.capstone.unwind.model.UserDTO;

import com.capstone.unwind.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "roleRoleName", target = "role.roleName")
    @Mapping(source = "roleId", target = "role.id")
    User toEntity(UserDto userDto);

    @InheritInverseConfiguration(name = "toEntity")
    UserDto toDto(User user);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);
}
