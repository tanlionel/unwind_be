package com.capstone.unwind.controller;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.exception.UserDoesNotHavePermission;
import com.capstone.unwind.model.PostingDTO.PostingDetailResponseDTO;
import com.capstone.unwind.model.PostingDTO.PostingResponseDTO;
import com.capstone.unwind.model.ResortDTO.ResortDetailResponseDTO;
import com.capstone.unwind.model.ResortDTO.ResortDto;
import com.capstone.unwind.model.ResortDTO.UnitTypeResponseDTO;
import com.capstone.unwind.model.RoomDTO.RoomInfoDto;
import com.capstone.unwind.model.RoomDTO.RoomRequestDTO;
import com.capstone.unwind.model.RoomDTO.RoomResponseDTO;
import com.capstone.unwind.model.SystemDTO.FaqDTO;
import com.capstone.unwind.model.SystemDTO.PolicyDTO;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyDto;
import com.capstone.unwind.service.ServiceInterface.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/public")
@RequiredArgsConstructor
@CrossOrigin
public class PublicController {
    @Autowired
    private final ResortService resortService;
    @Autowired
    private final FaqService faqService;
    @Autowired
    private final PolicyService policyService;
    @Autowired
    TimeshareCompanyService timeshareCompanyService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private RentalPostingService rentalPostingService;
    @GetMapping("/resort")
    public Page<ResortDto> getPageableResort(@RequestParam(required = false,defaultValue = "0") Integer pageNo,
                                             @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false,defaultValue = "") String resortName) throws UserDoesNotHavePermission {
        Page<ResortDto> resortDtoPage = resortService.getPublicPageableResort(pageNo,pageSize,resortName);
        return resortDtoPage;
    }
    @GetMapping("/resort/{resortId}")
    public ResortDetailResponseDTO getResortById(@PathVariable Integer resortId) throws EntityDoesNotExistException, UserDoesNotHavePermission {
        ResortDetailResponseDTO resortDetailResponseDTO = resortService.getPublicResortById(resortId);
        return resortDetailResponseDTO;
    }
    @GetMapping("/faq/all")
    public List<FaqDTO> getAllFaq() {
        return faqService.findAll();
    }
    @GetMapping("policy/{type}")
    public List<PolicyDTO> getPolicyByType(@PathVariable String type) throws EntityDoesNotExistException {
        List<PolicyDTO> policyDTO = policyService.getPolicyByType(type);
        return policyDTO;
    }
    @GetMapping("faq/{type}")
    public List<FaqDTO> getFaqByType(@PathVariable String type) throws EntityDoesNotExistException {
        List<FaqDTO> faqDTO = faqService.getFaqByType(type);
        return faqDTO;
    }
    @GetMapping("/policy/all")
    public List<PolicyDTO> getAllPolicy() {
        return policyService.findAll();
    }


    @GetMapping("/timeshare-company")
    public Page<TimeshareCompanyDto> getPageableTimeshareCompany(@RequestParam(required = false,defaultValue = "0") Integer pageNo,
                                                                 @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                                                 @RequestParam(required = false,defaultValue = "") String tsName){
        Page<TimeshareCompanyDto> timeshareCompanyDtoPage = timeshareCompanyService.getPageableTimeshareCompany(pageNo,pageSize,tsName);
        return timeshareCompanyDtoPage;
    }
    @GetMapping("timeshare-company/{tsId}")
    public TimeshareCompanyDto getTimeshareCompanyById(@PathVariable Integer tsId) throws EntityDoesNotExistException {
        TimeshareCompanyDto timeshareCompanyDto = timeshareCompanyService.getTimeshareCompanyById(tsId);
        return timeshareCompanyDto;
    }


    @GetMapping("/room/resort/{resortId}")
    public ResponseEntity<List<RoomInfoDto>> getAllRoomByResortId(@PathVariable Integer resortId){
        List<RoomInfoDto> roomInfoDtoList  = roomService.getAllExistingRoomByResortId(resortId);
        return ResponseEntity.ok(roomInfoDtoList);
    }
    @GetMapping("unit-type/{unitTypeId}")
    public ResponseEntity<UnitTypeResponseDTO> getUnitTypeById(@PathVariable Integer unitTypeId) throws UserDoesNotHavePermission, EntityDoesNotExistException {
        UnitTypeResponseDTO unitTypeResponse = resortService.getUnitTypeByIdPublic(unitTypeId);
        return new ResponseEntity<>(unitTypeResponse, HttpStatus.OK);

    }
    @GetMapping("/unit-types/resort/{resortId}")

    public ResponseEntity<List<UnitTypeResponseDTO>> getUnitTypeByResortId(
            @PathVariable Integer resortId) throws  EntityDoesNotExistException, ErrMessageException {

        List<UnitTypeResponseDTO> response = resortService.getUnitTypeByResortIdPublic(resortId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rental/postings")
    public Page<PostingResponseDTO> getPublicPostings(
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String resortName) throws OptionalNotFoundException {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<PostingResponseDTO> postingResponsePage = rentalPostingService.getAllPublicPostings(resortName, pageable);

        return postingResponsePage;
    }
    @GetMapping("rental/posting/{postingId}")
    public PostingDetailResponseDTO getRentalPostingDetail(@PathVariable Integer postingId) throws OptionalNotFoundException {
        return rentalPostingService.getRentalPostingDetailById(postingId);
    }
}
