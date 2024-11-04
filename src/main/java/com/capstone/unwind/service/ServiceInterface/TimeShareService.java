package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.TimeShareDTO.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TimeShareService {
    TimeShareResponseDTO createTimeShare(TimeShareRequestDTO timeShareRequestDTO) throws EntityDoesNotExistException, ErrMessageException, OptionalNotFoundException;
    Page<ListTimeShareDTO> getAllTimeShares(Pageable pageable) throws OptionalNotFoundException;
    TimeShareDetailDTO getTimeShareDetails(Integer timeShareID) throws OptionalNotFoundException;

    List<Integer> getTimeshareValidYears(Integer timeshareId) throws OptionalNotFoundException;
}
