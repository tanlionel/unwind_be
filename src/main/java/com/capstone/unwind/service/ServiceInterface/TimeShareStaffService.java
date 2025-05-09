package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.entity.TimeshareCompanyStaff;
import com.capstone.unwind.exception.*;
import com.capstone.unwind.model.AuthDTO.ResponseObjectDTO;
import com.capstone.unwind.model.TimeShareStaffDTO.LoginTSStaffRequestDto;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffRequestDTO;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareStaffUpdateRequestDTO;
import org.springframework.data.domain.Page;

public interface TimeShareStaffService {
    TimeShareCompanyStaffDTO createTimeshareStaff(TimeShareCompanyStaffRequestDTO timeShareCompanyStaffDTO)
            throws EntityAlreadyExist, UserDoesNotExistException, ErrMessageException;
    Page<TimeShareCompanyStaffDTO> getPageableTsStaff(Integer pageNo, Integer pageSize, String StaffName) throws UserDoesNotHavePermission;
    TimeShareCompanyStaffDTO updateTimeshareStaff(Integer staffId, TimeShareStaffUpdateRequestDTO timeShareCompanyStaffDTO)
            throws EntityDoesNotExistException, ErrMessageException;
    TimeShareCompanyStaffDTO getTimeshareStaffById(Integer tsStaffId) throws EntityDoesNotExistException;
    TimeshareCompanyStaff loginStaff(LoginTSStaffRequestDto loginTSStaffRequestDto) throws OptionalNotFoundException, AccountSuspendedException, InvalidateException;
    TimeShareCompanyStaffDTO getLoginStaff();
}
