package com.capstone.unwind.model.CustomerDTO;

import com.capstone.unwind.entity.Customer;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerInitMapper {
    @Mapping(source = "walletAvailableMoney", target = "wallet.availableMoney")
    @Mapping(source = "walletId", target = "wallet.id")
    @Mapping(source = "userRoleRoleName", target = "user.role.roleName")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "membershipName", target = "membership.name")
    Customer toEntity(CustomerInitDto customerDto);

    @InheritInverseConfiguration(name = "toEntity")
    CustomerInitDto toDto(Customer customer);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer partialUpdate(CustomerInitDto customerDto, @MappingTarget Customer customer);
}