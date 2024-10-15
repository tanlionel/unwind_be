package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.UserDoesNotHavePermission;
import com.capstone.unwind.model.ResortDTO.*;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ResortService {
    ResortDetailResponseDTO createResort(ResortRequestDTO resortDto) throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission;

    ResortDetailResponseDTO getResortById(Integer resortId) throws EntityDoesNotExistException, UserDoesNotHavePermission;

    Page<ResortDto> getPageableResort(Integer pageNo, Integer pageSize, String resortName) throws UserDoesNotHavePermission;
    ResortDetailResponseDTO getPublicResortById(Integer resortId) throws EntityDoesNotExistException, UserDoesNotHavePermission;

    Page<ResortDto> getPublicPageableResort(Integer pageNo, Integer pageSize, String resortName) throws UserDoesNotHavePermission;


    UnitTypeResponseDTO createUnitType(UnitTypeRequestDTO unitTypeRequestDTO) throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission;
    UnitTypeResponseDTO updateUnitType(Integer unitTypeId, UnitTypeRequestDTO unitTypeRequestDTO)
            throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission;
    UnitTypeResponseDTO getUnitTypeById(Integer unitTypeId)
            throws EntityDoesNotExistException, UserDoesNotHavePermission;
    UnitTypeResponseDTO getUnitTypeByIdPublic(Integer unitTypeId)
            throws EntityDoesNotExistException, UserDoesNotHavePermission;
    List<UnitTypeResponseDTO> getUnitTypeByResortId(Integer resortId)
            throws EntityDoesNotExistException, UserDoesNotHavePermission, ErrMessageException;
    List<UnitTypeResponseDTO> getUnitTypeByResortIdPublic(Integer resortId) throws ErrMessageException, EntityDoesNotExistException;

}
