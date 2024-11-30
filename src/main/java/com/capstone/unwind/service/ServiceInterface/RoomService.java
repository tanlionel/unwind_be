package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.exception.UserDoesNotHavePermission;
import com.capstone.unwind.model.RoomDTO.*;
import com.capstone.unwind.model.TimeShareDTO.UpdateTimeshareRequestDto;

import java.util.List;

public interface RoomService {
    RoomResponseDTO createRoom(RoomRequestDTO roomRequestDTO) throws EntityDoesNotExistException, UserDoesNotHavePermission, ErrMessageException;
    List<RoomInfoDto> getAllExistingRoomByResortId(Integer resortId);
    RoomDetailResponseDTO getRoomDetailById(Integer roomId) throws OptionalNotFoundException;
    UpdateRoomResponseDTO updateRoomAmenityByRoomId(Integer roomId, UpdateTimeshareRequestDto timeShareRequestDTO)
            throws ErrMessageException, OptionalNotFoundException;
}
