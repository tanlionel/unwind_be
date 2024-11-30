package com.capstone.unwind.controller;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.TimeShareDTO.*;
import com.capstone.unwind.service.ServiceInterface.TimeShareService;
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
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin
public class TimeshareController {
    @Autowired
    private final TimeShareService timeShareService;

    @PostMapping("/timeshare")
    public ResponseEntity<TimeShareResponseDTO> createTimeShare(@RequestBody TimeShareRequestDTO timeShareRequestDTO) throws EntityDoesNotExistException, ErrMessageException, OptionalNotFoundException {

        TimeShareResponseDTO timeShareResponse = timeShareService.createTimeShare(timeShareRequestDTO);
        return new ResponseEntity<>(timeShareResponse, HttpStatus.OK);
    }
    @PutMapping("/{timeshareId}")
    public ResponseEntity<UpdateTimeshareResponseDto> updateTimeshare(
            @PathVariable Integer timeshareId,
            @RequestBody  UpdateTimeshareDto updateTimeshareDto) throws OptionalNotFoundException {
            UpdateTimeshareResponseDto response = timeShareService.updateTimeShare(timeshareId, updateTimeshareDto);
            return ResponseEntity.ok(response);
    }
    @GetMapping("/timeshares")
    public ResponseEntity<Page<ListTimeShareDTO>> getAllTimeShares(  @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) throws OptionalNotFoundException{
        Pageable pageable = PageRequest.of(page, size);
        Page<ListTimeShareDTO> timeShares = timeShareService.getAllTimeShares(pageable);
        return ResponseEntity.ok(timeShares);
    }

    @GetMapping("/timeshare/{timeShareID}")
    public TimeShareDetailDTO getTimeShareDetails(@PathVariable Integer timeShareID) throws OptionalNotFoundException {
        return timeShareService.getTimeShareDetails(timeShareID);
    }
    @GetMapping("/timeshare/valid-year/{timeshareId}")
    public ResponseEntity<List<Integer>> getValidTimeshareYear(@PathVariable Integer timeshareId) throws OptionalNotFoundException {
        List<Integer> validYearsList = timeShareService.getTimeshareValidYears(timeshareId);
        return ResponseEntity.ok(validYearsList);
    }

}
