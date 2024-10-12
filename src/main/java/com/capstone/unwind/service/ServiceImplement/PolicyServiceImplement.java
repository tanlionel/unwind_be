package com.capstone.unwind.service.ServiceImplement;


import com.capstone.unwind.entity.Faq;
import com.capstone.unwind.entity.Policy;
import com.capstone.unwind.exception.EntityAlreadyExist;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.model.SystemDTO.*;
import com.capstone.unwind.repository.FagRespository;
import com.capstone.unwind.repository.PolicyRespository;
import com.capstone.unwind.service.ServiceInterface.FaqService;
import com.capstone.unwind.service.ServiceInterface.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PolicyServiceImplement implements PolicyService {
    @Autowired
    private final PolicyRespository policyRespository;
    @Autowired
    private final PolicyMapper policyMapper;

    @Override
    public PolicyDTO createPolicy(PolicyRequestDTO policyDTO) throws EntityAlreadyExist {
        Policy policyRequest = Policy.builder()
                .type(policyDTO.getType())
                .title(policyDTO.getTitle())
                .description(policyDTO.getDescription())
                .build();
        Policy policyDB = policyRespository.save(policyRequest);
        PolicyDTO policyDtoDB = policyMapper.toDto(policyDB);
        return policyDtoDB;
    }

    @Override
    public List<PolicyDTO> getPolicyByType(String type) throws EntityDoesNotExistException {
        List<Policy> policies = policyRespository.findAllByType(type);
        if (policies == null) throw new EntityDoesNotExistException();
        return policyMapper.toDtoList(policies);
    }

    @Override
    public PolicyDTO updatePolicy(Integer id, PolicyRequestDTO policyDTO) throws EntityDoesNotExistException {
        Policy policy = policyRespository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistException());

        policy.setType(policyDTO.getType());
        policy.setTitle(policyDTO.getTitle());
        policy.setDescription(policyDTO.getDescription());
        Policy updatedFaq = policyRespository.save(policy);
        return policyMapper.toDto(updatedFaq);
    }
    @Override
    public List<PolicyDTO> findAll() {
        List<Policy> policyList = policyRespository.findAll();
        return policyMapper.toDtoList(policyList);
    }
}