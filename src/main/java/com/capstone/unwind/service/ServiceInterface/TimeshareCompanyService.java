package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.EntityAlreadyExist;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.UserDoesNotExistException;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyDto;
import org.springframework.data.domain.Page;

public interface TimeshareCompanyService {
    TimeshareCompanyDto createTimeshareCompany(TimeshareCompanyDto timeshareCompanyDto) throws EntityAlreadyExist, UserDoesNotExistException, ErrMessageException;
    Page<TimeshareCompanyDto> getPageableTimeshareCompany(Integer pageNo,Integer pageSize,String tsName);
    TimeshareCompanyDto getTimeshareCompanyById(Integer tsId) throws EntityDoesNotExistException;

}
