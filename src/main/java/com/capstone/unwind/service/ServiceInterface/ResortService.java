package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.entity.ResortAmenity;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.model.ResortDTO.ResortAmenitiesRequestDTO;
import com.capstone.unwind.model.ResortDTO.ResortDetailResponseDTO;
import com.capstone.unwind.model.ResortDTO.ResortDto;
import com.capstone.unwind.model.ResortDTO.ResortPoliciesRequestDto;
import org.springframework.data.domain.Page;

public interface ResortService {
    ResortDto createResort(ResortDto resortDto) throws EntityDoesNotExistException;
    ResortDetailResponseDTO getResortById(Integer resortId) throws EntityDoesNotExistException;
    Page<ResortDto> getPageableResort(Integer pageNo, Integer pageSize, String resortName);
    Boolean createResortAmenities(ResortAmenitiesRequestDTO resortAmenitiesRequestDTO) throws EntityDoesNotExistException;
    Boolean createResortPolicies(ResortPoliciesRequestDto resortPoliciesRequestDto) throws EntityDoesNotExistException;
}
