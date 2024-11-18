package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.*;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyDto;
import com.capstone.unwind.model.TimeshareCompany.UpdateTimeshareCompanyDto;
import org.springframework.data.domain.Page;

public interface TimeshareCompanyService {
    TimeshareCompanyDto createTimeshareCompany(TimeshareCompanyDto timeshareCompanyDto) throws EntityAlreadyExist, UserDoesNotExistException, ErrMessageException;
    Page<TimeshareCompanyDto> getPageableTimeshareCompany(Integer pageNo,Integer pageSize,String tsName);
    TimeshareCompanyDto getTimeshareCompanyById(Integer tsId) throws EntityDoesNotExistException;
    TimeshareCompanyDto updateTimeshareCompany( UpdateTimeshareCompanyDto timeshareCompanyDto) throws   ErrMessageException, OptionalNotFoundException;
    TimeshareCompanyDto getProfileTimeshareCompanyById() throws EntityDoesNotExistException;
}
