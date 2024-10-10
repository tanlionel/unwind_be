package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.entity.ResortAmenity;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.model.ResortDTO.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ResortService {
    ResortDetailResponseDTO createResort(ResortRequestDTO resortDto) throws EntityDoesNotExistException, ErrMessageException;
    ResortDetailResponseDTO getResortById(Integer resortId) throws EntityDoesNotExistException;
    Page<ResortDto> getPageableResort(Integer pageNo, Integer pageSize, String resortName);
    List<UnitTypeDto> createUnitType(ResortUnitTypeRequestDTO resortUnitTypeRequestDTO) throws EntityDoesNotExistException, ErrMessageException;
}
