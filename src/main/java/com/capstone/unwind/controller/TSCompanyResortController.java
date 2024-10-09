package com.capstone.unwind.controller;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.model.ResortDTO.ResortAmenitiesRequestDTO;
import com.capstone.unwind.model.ResortDTO.ResortDetailResponseDTO;
import com.capstone.unwind.model.ResortDTO.ResortDto;
import com.capstone.unwind.model.ResortDTO.ResortPoliciesRequestDto;
import com.capstone.unwind.service.ServiceInterface.ResortService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResortDto createResort(@RequestBody ResortDto resortDto) throws EntityDoesNotExistException {
        ResortDto resortDtoResponse = resortService.createResort(resortDto);
        return resortDtoResponse;
    }
    @PostMapping("/resort-amenities")
    public ResponseEntity<String> createResortAmenities(@RequestBody ResortAmenitiesRequestDTO resortAmenitiesRequestDTO) throws EntityDoesNotExistException {
        Boolean flag = resortService.createResortAmenities(resortAmenitiesRequestDTO);
        if (!flag) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("create fail");
        return ResponseEntity.ok("Successful");
    }
    @PostMapping("/resort-policies")
    public ResponseEntity<String> createResortPolicies(@RequestBody ResortPoliciesRequestDto resortPoliciesRequestDto) throws EntityDoesNotExistException {
        Boolean flag = resortService.createResortPolicies(resortPoliciesRequestDto);
        if (!flag) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("create fail");
        return ResponseEntity.ok("Successful");
    }
}
