package com.capstone.unwind.controller;

import com.capstone.unwind.exception.*;
import com.capstone.unwind.model.ResortDTO.*;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffRequestDTO;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareStaffUpdateRequestDTO;
import com.capstone.unwind.service.ServiceInterface.ResortService;
import com.capstone.unwind.service.ServiceInterface.TimeShareStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timeshare-company/staff")
@RequiredArgsConstructor
@CrossOrigin
public class TSCompanyStaffController {
    @Autowired
    private final ResortService resortService;
    @Autowired
    TimeShareStaffService timeshareCompanyStaffService;

    @PostMapping()
    public TimeShareCompanyStaffDTO createTSCompanyStaff(@RequestBody TimeShareCompanyStaffRequestDTO timeShareCompanyStaffDTO) throws EntityAlreadyExist, UserDoesNotExistException, ErrMessageException {
        TimeShareCompanyStaffDTO timeshareCompanyStaffDtoResponse = timeshareCompanyStaffService.createTimeshareStaff(timeShareCompanyStaffDTO);
        return timeshareCompanyStaffDtoResponse;
    }
    @GetMapping()
    public Page<TimeShareCompanyStaffDTO> getPageableTSCompanyStaff(@RequestParam(required = false,defaultValue = "0") Integer pageNo,
                                             @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false,defaultValue = "") String StaffName) throws UserDoesNotHavePermission {
        Page<TimeShareCompanyStaffDTO> TSCompanyStaffDtoPage = timeshareCompanyStaffService.getPageableTsStaff(pageNo,pageSize,StaffName);
        return TSCompanyStaffDtoPage;
    }
    @PutMapping("/{staffId}")
    public ResponseEntity<TimeShareCompanyStaffDTO> updateTimeshareStaff(
            @PathVariable Integer staffId,
            @RequestBody TimeShareStaffUpdateRequestDTO timeShareCompanyStaffDTO) throws ErrMessageException, EntityDoesNotExistException {
        TimeShareCompanyStaffDTO updatedStaff = timeshareCompanyStaffService.updateTimeshareStaff(staffId, timeShareCompanyStaffDTO);
        return ResponseEntity.ok(updatedStaff);
    }
}
