package com.capstone.unwind.controller;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.model.TimeShareDTO.*;
import com.capstone.unwind.service.ServiceInterface.FaqService;
import com.capstone.unwind.service.ServiceInterface.TimeShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin
public class TimeShareController {
    @Autowired
    private final TimeShareService timeShareService;

    @PostMapping("/timeshare")
    public ResponseEntity<TimeShareResponseDTO> createTimeShare(@RequestBody TimeShareRequestDTO timeShareRequestDTO) throws EntityDoesNotExistException, ErrMessageException {

        TimeShareResponseDTO timeShareResponse = timeShareService.createTimeShare(timeShareRequestDTO);
        return new ResponseEntity<>(timeShareResponse, HttpStatus.OK);
    }
    @GetMapping("/timeshare/all")
    public ResponseEntity<List<ListTimeShareDTO>> getAllTimeShares() {
        List<ListTimeShareDTO> timeShares = timeShareService.getAllTimeShares();
        return ResponseEntity.ok(timeShares);
    }
    @GetMapping("/{timeShareID}")
    public List<TimeShareDetailDTO> getTimeShareDetails(@PathVariable Integer timeShareID) {
        return timeShareService.getTimeShareDetails(timeShareID);
    }
}
