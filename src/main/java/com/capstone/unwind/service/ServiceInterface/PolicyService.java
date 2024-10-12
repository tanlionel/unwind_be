package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.EntityAlreadyExist;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.model.SystemDTO.FaqDTO;
import com.capstone.unwind.model.SystemDTO.PolicyDTO;
import com.capstone.unwind.model.SystemDTO.PolicyRequestDTO;

import java.util.List;

public interface PolicyService {
    PolicyDTO createPolicy(PolicyRequestDTO policyDTO) throws EntityAlreadyExist;
    List<PolicyDTO> getPolicyByType(String type) throws EntityDoesNotExistException;
    PolicyDTO updatePolicy(Integer id, PolicyRequestDTO policyDTO) throws EntityDoesNotExistException;
    List<PolicyDTO> findAll();
}
