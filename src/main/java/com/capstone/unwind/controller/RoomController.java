package com.capstone.unwind.controller;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.UserDoesNotHavePermission;
import com.capstone.unwind.model.RoomDTO.RoomRequestDTO;
import com.capstone.unwind.model.RoomDTO.RoomResponseDTO;
import com.capstone.unwind.service.ServiceInterface.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/room")
@RequiredArgsConstructor
@CrossOrigin
public class RoomController {
    @Autowired
    private RoomService roomService;
    @PostMapping()
    public ResponseEntity<RoomResponseDTO> createRoom(@RequestBody RoomRequestDTO roomRequestDTO) throws UserDoesNotHavePermission, EntityDoesNotExistException, ErrMessageException {
            RoomResponseDTO roomResponseDTO = roomService.createRoom(roomRequestDTO);
            return new ResponseEntity<>(roomResponseDTO, HttpStatus.OK);
    }
}
