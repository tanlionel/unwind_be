package com.capstone.unwind.controller;

import com.capstone.unwind.exception.*;
import com.capstone.unwind.model.ResortDTO.*;
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
    public UnitTypeResponseDTO createUnitType(@RequestBody UnitTypeRequestDTO unitTypeRequestDTO) throws ErrMessageException, EntityDoesNotExistException, UserDoesNotHavePermission {
        UnitTypeResponseDTO unitTypeDetailResponseDTO = resortService.createUnitType(unitTypeRequestDTO);
        return unitTypeDetailResponseDTO;
    }
    @PutMapping("unit-type/{unitTypeId}")
    public ResponseEntity<UnitTypeResponseDTO> updateUnitType(
            @PathVariable Integer unitTypeId,
            @RequestBody UnitTypeRequestDTO unitTypeRequestDTO) throws UserDoesNotHavePermission, ErrMessageException, EntityDoesNotExistException {
        UnitTypeResponseDTO response = resortService.updateUnitType(unitTypeId, unitTypeRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("unit-type/{unitTypeId}")
    public ResponseEntity<UnitTypeResponseDTO> getUnitTypeById(@PathVariable Integer unitTypeId) throws UserDoesNotHavePermission, EntityDoesNotExistException {
            UnitTypeResponseDTO unitTypeResponse = resortService.getUnitTypeById(unitTypeId);
            return new ResponseEntity<>(unitTypeResponse, HttpStatus.OK);

    }
    @GetMapping("/unit-types/{resortId}")

    public ResponseEntity<List<UnitTypeResponseDTO>> getUnitTypeByResortId(
            @PathVariable Integer resortId) throws UserDoesNotHavePermission, EntityDoesNotExistException, ErrMessageException {

        List<UnitTypeResponseDTO> response = resortService.getUnitTypeByResortId(resortId);
        return ResponseEntity.ok(response);
    }

}
