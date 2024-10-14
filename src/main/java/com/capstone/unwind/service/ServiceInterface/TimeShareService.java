package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.model.TimeShareDTO.*;

import java.util.List;

public interface TimeShareService {
    TimeShareResponseDTO createTimeShare(TimeShareRequestDTO timeShareRequestDTO) throws EntityDoesNotExistException, ErrMessageException;
    List<ListTimeShareDTO> getAllTimeShares();
    List<TimeShareDetailDTO> getTimeShareDetails(Integer timeShareID);
}
