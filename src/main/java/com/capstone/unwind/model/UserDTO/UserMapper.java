package com.capstone.unwind.model.UserDTO;

import com.capstone.unwind.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "roleName", target = "role.roleName")
    @Mapping(source = "roleId", target = "role.id")
    User toEntity(UserDto userDto);

    //@Mapping(source = "userName", target = "userName")
    @Mapping(source = "username", target = "userName")
    @Mapping(source = "role.roleName", target = "roleName")
    @Mapping(source = "role.id",target = "roleId")
    UserDto toDto(User user);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);
}
