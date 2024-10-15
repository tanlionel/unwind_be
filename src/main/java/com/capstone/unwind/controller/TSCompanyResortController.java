package com.capstone.unwind.controller;

import com.capstone.unwind.entity.UnitType;
import com.capstone.unwind.exception.*;
import com.capstone.unwind.model.ResortDTO.*;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;
import com.capstone.unwind.service.ServiceInterface.ResortService;
import com.capstone.unwind.service.ServiceInterface.TimeShareStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timeshare-company/resort")
@RequiredArgsConstructor
@CrossOrigin
public class TSCompanyResortController {
    @Autowired
    private final ResortService resortService;
    @Autowired
    TimeShareStaffService timeshareCompanyStaffService;

    @GetMapping()
    public Page<ResortDto> getPageableResort(@RequestParam(required = false,defaultValue = "0") Integer pageNo,
                                             @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false,defaultValue = "") String resortName) throws UserDoesNotHavePermission {
        Page<ResortDto> resortDtoPage = resortService.getPageableResort(pageNo,pageSize,resortName);
        return resortDtoPage;
    }
    @GetMapping("{resortId}")
    public ResortDetailResponseDTO getResortById(@PathVariable Integer resortId) throws EntityDoesNotExistException, UserDoesNotHavePermission {
        ResortDetailResponseDTO resortDetailResponseDTO = resortService.getResortById(resortId);
        return resortDetailResponseDTO;
    }
    @PostMapping()
    public ResortDetailResponseDTO createResort(@RequestBody ResortRequestDTO resortDto) throws EntityDoesNotExistException, ErrMessageException, UserDoesNotHavePermission {
        ResortDetailResponseDTO resortDtoResponse = resortService.createResort(resortDto);
        return resortDtoResponse;
    }
    @PostMapping("unit-type")
    public AddUnitTypeAmentiesResponseDTO createUnitType(@RequestBody AddUnitTypeAmentiesDTO addUnitTypeAmentiesDTO) throws ErrMessageException, EntityDoesNotExistException, UserDoesNotHavePermission {
        AddUnitTypeAmentiesResponseDTO unitTypeDetailResponseDTO = resortService.createUnitType(addUnitTypeAmentiesDTO);
        return unitTypeDetailResponseDTO;
    }
    @PutMapping("unit-type/{unitTypeId}")
    public ResponseEntity<AddUnitTypeAmentiesResponseDTO> updateUnitType(
            @PathVariable Integer unitTypeId,
            @RequestBody AddUnitTypeAmentiesDTO addUnitTypeAmentiesDTO) throws UserDoesNotHavePermission, ErrMessageException, EntityDoesNotExistException {
        AddUnitTypeAmentiesResponseDTO response = resortService.updateUnitType(unitTypeId, addUnitTypeAmentiesDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("unit-type/{unitTypeId}")
    public ResponseEntity<AddUnitTypeAmentiesResponseDTO> getUnitTypeById(@PathVariable Integer unitTypeId) throws UserDoesNotHavePermission, EntityDoesNotExistException {
            AddUnitTypeAmentiesResponseDTO unitTypeResponse = resortService.getUnitTypeById(unitTypeId);
            return new ResponseEntity<>(unitTypeResponse, HttpStatus.OK);

    }
    @GetMapping("/unit-types/{resortId}")
    public ResponseEntity<AddUnitTypeAmentiesResponseDTO> getUnitTypeByResortId(
            @PathVariable Integer resortId) throws UserDoesNotHavePermission, EntityDoesNotExistException {

        AddUnitTypeAmentiesResponseDTO response = resortService.getUnitTypeByResortId(resortId);
        return ResponseEntity.ok(response);
    }

}
