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
    public List<UnitTypeDto> createUnitType(@RequestBody ResortUnitTypeRequestDTO resortUnitTypeRequestDTO) throws ErrMessageException, EntityDoesNotExistException, UserDoesNotHavePermission {
        List<UnitTypeDto> unitTypeDtoList = resortService.createUnitType(resortUnitTypeRequestDTO);
        return unitTypeDtoList;
    }
    @PostMapping("/staff")
    public TimeShareCompanyStaffDTO createTSCompanyStaff(@RequestBody TimeShareCompanyStaffDTO timeShareCompanyStaffDTO) throws EntityAlreadyExist, UserDoesNotExistException, ErrMessageException {
        TimeShareCompanyStaffDTO timeshareCompanyStaffDtoResponse = timeshareCompanyStaffService.createTimeshareStaff(timeShareCompanyStaffDTO);
        return timeshareCompanyStaffDtoResponse;
    }
    @GetMapping("/staff")
    public Page<TimeShareCompanyStaffDTO> getPageableTSCompanyStaff(@RequestParam(required = false,defaultValue = "0") Integer pageNo,
                                             @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false,defaultValue = "") String StaffName) throws UserDoesNotHavePermission {
        Page<TimeShareCompanyStaffDTO> TSCompanyStaffDtoPage = timeshareCompanyStaffService.getPageableTsStaff(pageNo,pageSize,StaffName);
        return TSCompanyStaffDtoPage;
    }
    @PostMapping("unit-type_amentities")
    public List<UnitTypeAmenitiesDTO> createUnitType(@RequestBody AddUnitTypeAmentiesDTO addUnitTypeAmentiesDTO) throws ErrMessageException, EntityDoesNotExistException, UserDoesNotHavePermission {
        List<UnitTypeAmenitiesDTO> unitTypeDtoList = resortService.addAmenitiesToUnitType(addUnitTypeAmentiesDTO);
        return unitTypeDtoList;
    }
    @PutMapping("/{staffId}")
    public ResponseEntity<TimeShareCompanyStaffDTO> updateTimeshareStaff(
            @PathVariable Integer staffId,
            @RequestBody TimeShareCompanyStaffDTO timeShareCompanyStaffDTO) throws ErrMessageException, EntityDoesNotExistException {
        TimeShareCompanyStaffDTO updatedStaff = timeshareCompanyStaffService.updateTimeshareStaff(staffId, timeShareCompanyStaffDTO);
        return ResponseEntity.ok(updatedStaff);
    }
}
