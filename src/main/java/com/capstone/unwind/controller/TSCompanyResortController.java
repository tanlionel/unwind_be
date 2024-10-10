package com.capstone.unwind.controller;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.model.ResortDTO.*;
import com.capstone.unwind.service.ServiceInterface.ResortService;
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
    @GetMapping()
    public Page<ResortDto> getPageableResort(@RequestParam(required = false,defaultValue = "0") Integer pageNo,
                                             @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false,defaultValue = "") String resortName){
        Page<ResortDto> resortDtoPage = resortService.getPageableResort(pageNo,pageSize,resortName);
        return resortDtoPage;
    }
    @GetMapping("{resortId}")
    public ResortDetailResponseDTO getResortById(@PathVariable Integer resortId) throws EntityDoesNotExistException {
        ResortDetailResponseDTO resortDetailResponseDTO = resortService.getResortById(resortId);
        return resortDetailResponseDTO;
    }
    @PostMapping()
    public ResortDetailResponseDTO createResort(@RequestBody ResortRequestDTO resortDto) throws EntityDoesNotExistException, ErrMessageException {
        ResortDetailResponseDTO resortDtoResponse = resortService.createResort(resortDto);
        return resortDtoResponse;
    }
    @PostMapping("unit-type")
    public List<UnitTypeDto> createUnitType(@RequestBody ResortUnitTypeRequestDTO resortUnitTypeRequestDTO) throws ErrMessageException, EntityDoesNotExistException {
        List<UnitTypeDto> unitTypeDtoList = resortService.createUnitType(resortUnitTypeRequestDTO);
        return unitTypeDtoList;
    }
}
