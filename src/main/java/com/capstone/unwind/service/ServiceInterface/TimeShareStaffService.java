package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.*;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;
import org.springframework.data.domain.Page;

public interface TimeShareStaffService {
    TimeShareCompanyStaffDTO createTimeshareStaff(TimeShareCompanyStaffDTO timeShareCompanyStaffDTO)
            throws EntityAlreadyExist, UserDoesNotExistException, ErrMessageException;
    Page<TimeShareCompanyStaffDTO> getPageableTsStaff(Integer pageNo, Integer pageSize, String StaffName) throws UserDoesNotHavePermission;
    TimeShareCompanyStaffDTO updateTimeshareStaff(Integer staffId, TimeShareCompanyStaffDTO timeShareCompanyStaffDTO)
            throws EntityDoesNotExistException, ErrMessageException;
}
