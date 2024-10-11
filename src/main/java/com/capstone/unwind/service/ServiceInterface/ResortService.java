package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.UserDoesNotHavePermission;
import com.capstone.unwind.model.ResortDTO.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ResortService {
    ResortDetailResponseDTO createResort(ResortRequestDTO resortDto) throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission;

    ResortDetailResponseDTO getResortById(Integer resortId) throws EntityDoesNotExistException, UserDoesNotHavePermission;

    Page<ResortDto> getPageableResort(Integer pageNo, Integer pageSize, String resortName) throws UserDoesNotHavePermission;

    List<UnitTypeDto> createUnitType(ResortUnitTypeRequestDTO resortUnitTypeRequestDTO) throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission;

    AddUnitTypeAmentiesResponseDTO addAmenitiesToUnitType(AddUnitTypeAmentiesDTO addUnitTypeAmentiesDTO) throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission;
}
