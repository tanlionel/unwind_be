package com.capstone.unwind.model.SystemDTO;

import com.capstone.unwind.entity.Faq;
import com.capstone.unwind.entity.Policy;
import org.mapstruct.*;

import java.util.List;
@Mapper(componentModel = "spring")
public interface PolicyMapper {
    @Mapping(source = "policyId", target = "id")
    Policy toEntity(PolicyDTO policyDTO);

    @Mapping(source = "id", target = "policyId")
    PolicyDTO toDto(Policy policy);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "policyId", target = "id")
    Policy partialUpdate(PolicyDTO policyDTO, @MappingTarget Policy policy);

    List<Policy> toEntityList(List<PolicyDTO> policyDTOs);
    List<PolicyDTO> toDtoList(List<Policy> policies);
}
