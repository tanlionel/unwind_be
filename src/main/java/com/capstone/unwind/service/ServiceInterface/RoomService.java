package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.exception.UserDoesNotHavePermission;
import com.capstone.unwind.model.RoomDTO.RoomDetailResponseDTO;
import com.capstone.unwind.model.RoomDTO.RoomInfoDto;
import com.capstone.unwind.model.RoomDTO.RoomRequestDTO;
import com.capstone.unwind.model.RoomDTO.RoomResponseDTO;

import java.util.List;

public interface RoomService {
    RoomResponseDTO createRoom(RoomRequestDTO roomRequestDTO) throws EntityDoesNotExistException, UserDoesNotHavePermission, ErrMessageException;
    List<RoomInfoDto> getAllExistingRoomByResortId(Integer resortId);
    RoomDetailResponseDTO getRoomDetailById(Integer roomId) throws OptionalNotFoundException;
}
