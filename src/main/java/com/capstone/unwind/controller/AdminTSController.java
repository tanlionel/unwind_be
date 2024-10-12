package com.capstone.unwind.controller;

import com.capstone.unwind.entity.TimeshareCompanyStaff;
import com.capstone.unwind.exception.EntityAlreadyExist;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.UserDoesNotExistException;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyDto;
import com.capstone.unwind.repository.TimeshareCompanyRepository;
import com.capstone.unwind.service.ServiceInterface.TimeShareStaffService;
import com.capstone.unwind.service.ServiceInterface.TimeshareCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/timeshare-company")
@RequiredArgsConstructor
@CrossOrigin
public class AdminTSController {
    @Autowired
    TimeshareCompanyService timeshareCompanyService;


    @GetMapping()
    public Page<TimeshareCompanyDto> getPageableTimeshareCompany(@RequestParam(required = false,defaultValue = "0") Integer pageNo,
                                                                 @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                                                 @RequestParam(required = false,defaultValue = "") String tsName){
        Page<TimeshareCompanyDto> timeshareCompanyDtoPage = timeshareCompanyService.getPageableTimeshareCompany(pageNo,pageSize,tsName);
        return timeshareCompanyDtoPage;
    }
    @GetMapping("{tsId}")
    public TimeshareCompanyDto getTimeshareCompanyById(@PathVariable Integer tsId) throws EntityDoesNotExistException {
        TimeshareCompanyDto timeshareCompanyDto = timeshareCompanyService.getTimeshareCompanyById(tsId);
        return timeshareCompanyDto;
    }
    @PostMapping()
    public TimeshareCompanyDto createTSCompany(@RequestBody TimeshareCompanyDto timeshareCompanyDto) throws EntityAlreadyExist, UserDoesNotExistException, ErrMessageException {
        TimeshareCompanyDto timeshareCompanyDtoResponse = timeshareCompanyService.createTimeshareCompany(timeshareCompanyDto);
        return timeshareCompanyDtoResponse;
    }


}
