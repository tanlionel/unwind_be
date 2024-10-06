package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.TimeshareCompany;
import com.capstone.unwind.entity.User;
import com.capstone.unwind.exception.EntityAlreadyExist;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.UserDoesNotExistException;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyDto;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyMapper;
import com.capstone.unwind.repository.TimeshareCompanyRepository;
import com.capstone.unwind.repository.UserRepository;
import com.capstone.unwind.service.ServiceInterface.TimeshareCompanyService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TimeshareCompanyServiceImplement implements TimeshareCompanyService {
    @Autowired
    private final TimeshareCompanyMapper timeshareCompanyMapper;
    @Autowired
    private final TimeshareCompanyRepository timeshareCompanyRepository;
    @Autowired
    private final UserRepository userRepository;
    @Override
    public TimeshareCompanyDto createTimeshareCompany(TimeshareCompanyDto timeshareCompanyDto) throws EntityAlreadyExist, UserDoesNotExistException {
        User user = userRepository.findUserById(timeshareCompanyDto.getOwnerId());
        if (user==null) throw new UserDoesNotExistException();
        TimeshareCompany timeshareCompanyRequest = TimeshareCompany.builder()
                .timeshareCompanyName(timeshareCompanyDto.getTimeshareCompanyName())
                .logo(timeshareCompanyDto.getLogo())
                .address(timeshareCompanyDto.getAddress())
                .description(timeshareCompanyDto.getDescription())
                .contact(timeshareCompanyDto.getContact())
                .isActive(true)
                .owner(user)
                .build();
        TimeshareCompany timeshareCompanyDB = timeshareCompanyRepository.save(timeshareCompanyRequest);
        TimeshareCompanyDto timeshareCompanyDtoDB = timeshareCompanyMapper.toDto(timeshareCompanyDB);
        return timeshareCompanyDtoDB;
    }

    @Override
    public Page<TimeshareCompanyDto> getPageableTimeshareCompany(Integer pageNo, Integer pageSize, String tsName) {
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("id").ascending());
        Page<TimeshareCompany> timeshareCompanyPage = timeshareCompanyRepository.findAllByTimeshareCompanyNameContaining(tsName,pageable);
        Page<TimeshareCompanyDto> timeshareCompanyDtoPage = timeshareCompanyPage.map(timeshareCompanyMapper::toDto);
        return timeshareCompanyDtoPage;
    }

    @Override
    public TimeshareCompanyDto getTimeshareCompanyById(Integer tsId) throws EntityDoesNotExistException {
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyById(tsId);
        if (timeshareCompany==null) throw new EntityDoesNotExistException();
        return timeshareCompanyMapper.toDto(timeshareCompany);
    }
}
