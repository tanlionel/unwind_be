package com.capstone.unwind.model.CustomerDTO;

import com.capstone.unwind.entity.Customer;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {
    @Mapping(source = "membershipId", target = "membership.id")
    Customer toEntity(ProfileDto profileDto);

    @Mapping(source = "membership.id", target = "membershipId")
    @Mapping(source = "membership.name", target = "membershipName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "userUserName")
    @Mapping(source = "user.email", target = "userEmail")
    ProfileDto toDto(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "membershipId", target = "membership.id")
    @Mapping(source = "userId", target = "user.id")
    Customer partialUpdate(ProfileDto profileDto, @MappingTarget Customer customer);
}