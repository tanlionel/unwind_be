package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.exception.*;
import com.capstone.unwind.model.ResortDTO.ResortDto;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffRequestDTO;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareStaffUpdateRequestDTO;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeshareCompanyStaffMapper;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyDto;
import com.capstone.unwind.model.UserDTO.UpdateUserRequestDTO;
import com.capstone.unwind.model.UserDTO.UserDto;
import com.capstone.unwind.model.UserDTO.UserMapper;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.ResortService;
import com.capstone.unwind.service.ServiceInterface.TimeShareStaffService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TimeShareStaffServiceImplement implements TimeShareStaffService {
    @Autowired
    private final TimeshareCompanyStaffMapper timeshareCompanyStaffMapper;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TimeshareCompanyRepository timeshareCompanyRepository;
    @Autowired
    private final TimeshareCompanyStaffRepository timeshareCompanyStaffRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserService userService;
    @Autowired
    private final ResortRepository resortRepository;
    @Override
    public TimeShareCompanyStaffDTO createTimeshareStaff(TimeShareCompanyStaffRequestDTO timeShareCompanyStaffDTO)
            throws EntityAlreadyExist, UserDoesNotExistException, ErrMessageException {
        User tsCompany = userService.getLoginUser();
        User user = userRepository.findUserById(tsCompany.getId());
        if (user==null) throw new UserDoesNotExistException();
        if (user.getRole().getId() != 2) throw new ErrMessageException("Must be timeshare company role");
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(user.getId());

        Integer companyId = timeshareCompany.getId();
        TimeshareCompany existingCompany = timeshareCompanyRepository.findById(companyId)
                .orElseThrow(() -> new ErrMessageException("Timeshare company not found with ID: " + companyId));

        boolean userNameExists = timeshareCompanyStaffRepository.existsByUserNameAndTimeshareCompany_Id(
                timeShareCompanyStaffDTO.getUserName(), companyId);

        if (userNameExists) {
            throw new ErrMessageException("User already exist in  timeshare company");
        }

        TimeshareCompanyStaff timeshareStaffRequest = TimeshareCompanyStaff.builder()
                .timeshareCompany(existingCompany)
                .userName(timeShareCompanyStaffDTO.getUserName())
                .password(passwordEncoder.encode(timeShareCompanyStaffDTO.getPassword().trim()))
                .isActive(true)
                .build();
        TimeshareCompanyStaff timeshareCompanyStaffDB = timeshareCompanyStaffRepository.save(timeshareStaffRequest);
        return timeshareCompanyStaffMapper.toDto(timeshareCompanyStaffDB);
    }
    @Override
    public Page<TimeShareCompanyStaffDTO> getPageableTsStaff(Integer pageNo, Integer pageSize, String staffName) throws UserDoesNotHavePermission {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id"));
        User tsCompany = userService.getLoginUser();
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyByOwnerId(tsCompany.getId());
        if (timeshareCompany == null) {
            throw new UserDoesNotHavePermission();
        }
        Page<TimeshareCompanyStaff> staffPage;
        if (staffName == null || staffName.isEmpty()) {
            staffPage = timeshareCompanyStaffRepository.findAllByTimeshareCompanyId(timeshareCompany.getId(), pageable);
        } else {
            staffPage = timeshareCompanyStaffRepository.findAllByUserNameContainingAndTimeshareCompanyId(staffName, timeshareCompany.getId(), pageable);
        }
        Page<TimeShareCompanyStaffDTO> staffDtoPage = staffPage.map(timeshareCompanyStaffMapper::toDto);
        return staffDtoPage;
    }
    @Override
    public TimeShareCompanyStaffDTO updateTimeshareStaff(Integer staffId, TimeShareStaffUpdateRequestDTO timeShareCompanyStaffDTO)
            throws EntityDoesNotExistException, ErrMessageException {

        User tsCompany = userService.getLoginUser();
        Resort resort = resortRepository.findById(timeShareCompanyStaffDTO.getResortId())
                .orElseThrow(() -> new ErrMessageException("Resort not found with id: " + timeShareCompanyStaffDTO.getResortId()));
        if (tsCompany.getRole().getId() != 2) throw new ErrMessageException("Must be timeshare company role");


        TimeshareCompanyStaff existingStaff = timeshareCompanyStaffRepository.findById(staffId)
                .orElseThrow(() -> new EntityDoesNotExistException());
        existingStaff.setResort(resort);
        existingStaff.setIsActive(timeShareCompanyStaffDTO.getIsActive());
        TimeshareCompanyStaff updatedStaff = timeshareCompanyStaffRepository.save(existingStaff);
        return timeshareCompanyStaffMapper.toDto(updatedStaff);
    }
    @Override
    public TimeShareCompanyStaffDTO getTimeshareStaffById(Integer tsStaffId) throws EntityDoesNotExistException {
        Optional<TimeshareCompanyStaff> timeshareCompanyStaff = timeshareCompanyStaffRepository.findById(tsStaffId);
        if (timeshareCompanyStaff==null) throw new EntityDoesNotExistException();
        return timeshareCompanyStaffMapper.toDto(timeshareCompanyStaff.get());
    }
}
