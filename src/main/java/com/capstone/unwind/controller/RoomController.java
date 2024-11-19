package com.capstone.unwind.controller;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.exception.UserDoesNotHavePermission;
import com.capstone.unwind.model.RoomDTO.RoomDetailResponseDTO;
import com.capstone.unwind.model.RoomDTO.RoomInfoDto;
import com.capstone.unwind.model.RoomDTO.RoomRequestDTO;
import com.capstone.unwind.model.RoomDTO.RoomResponseDTO;
import com.capstone.unwind.model.TimeShareDTO.TimeShareResponseDTO;
import com.capstone.unwind.model.TimeShareDTO.UpdateTimeshareRequestDto;
import com.capstone.unwind.service.ServiceInterface.RoomService;
import com.capstone.unwind.service.ServiceInterface.TimeShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/room")
@RequiredArgsConstructor
@CrossOrigin
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private TimeShareService timeshareService;
    @PostMapping()
    public ResponseEntity<RoomResponseDTO> createRoom(@RequestBody RoomRequestDTO roomRequestDTO) throws UserDoesNotHavePermission, EntityDoesNotExistException, ErrMessageException {
            RoomResponseDTO roomResponseDTO = roomService.createRoom(roomRequestDTO);
            return new ResponseEntity<>(roomResponseDTO, HttpStatus.OK);
    }
    @GetMapping("/resort/{resortId}")
    public ResponseEntity<List<RoomInfoDto>> getAllRoomByResortId(@PathVariable Integer resortId){
        List<RoomInfoDto> roomInfoDtoList  = roomService.getAllExistingRoomByResortId(resortId);
        return ResponseEntity.ok(roomInfoDtoList);
    }
    @PutMapping("/room-amenity/{timeShareId}")
    public ResponseEntity<TimeShareResponseDTO> updateTimeshare(
            @PathVariable Integer timeShareId,
            @RequestBody UpdateTimeshareRequestDto updateTimeshareRequestDto) throws ErrMessageException, OptionalNotFoundException {
            TimeShareResponseDTO updatedTimeshare = timeshareService.updateTimeshare(timeShareId, updateTimeshareRequestDto);
            return ResponseEntity.ok(updatedTimeshare);
    }
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDetailResponseDTO> getRoomDetailById(@PathVariable Integer roomId) throws  OptionalNotFoundException {
        RoomDetailResponseDTO roomResponseDTO = roomService.getRoomDetailById(roomId);
        return ResponseEntity.ok(roomResponseDTO);
    }
}
