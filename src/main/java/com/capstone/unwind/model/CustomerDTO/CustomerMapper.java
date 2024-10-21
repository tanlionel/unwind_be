package com.capstone.unwind.model.CustomerDTO;

import com.capstone.unwind.entity.Customer;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    @Mapping(source = "membershipId", target = "membership.id")
    Customer toEntity(CustomerDto customerDto);

    @Mapping(source = "membership.id", target = "membershipId")
    CustomerDto toDto(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "membershipId", target = "membership.id")
    Customer partialUpdate(CustomerDto customerDto, @MappingTarget Customer customer);
}