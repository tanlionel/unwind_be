package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.*;
import com.capstone.unwind.exception.*;
import com.capstone.unwind.model.AuthDTO.ResponseObjectDTO;
import com.capstone.unwind.model.ResortDTO.ResortDto;
import com.capstone.unwind.model.TimeShareStaffDTO.*;
import com.capstone.unwind.model.TimeshareCompany.TimeshareCompanyDto;
import com.capstone.unwind.model.UserDTO.UpdateUserRequestDTO;
import com.capstone.unwind.model.UserDTO.UserDto;
import com.capstone.unwind.model.UserDTO.UserMapper;
import com.capstone.unwind.repository.*;
import com.capstone.unwind.service.ServiceInterface.JwtService;
import com.capstone.unwind.service.ServiceInterface.ResortService;
import com.capstone.unwind.service.ServiceInterface.TimeShareStaffService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
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
    private static final String SECRET_KEY = "ownrJE4LNVXTBOUdVZ2xmJ7VSDNhKTRJsagLsdS3jLfsOY91basfKf";


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

    @Override
    public TimeshareCompanyStaff loginStaff(LoginTSStaffRequestDto loginTSStaffRequestDto) throws OptionalNotFoundException, AccountSuspendedException, InvalidateException {
        TimeshareCompany timeshareCompany = timeshareCompanyRepository.findTimeshareCompanyById(loginTSStaffRequestDto.getTsCompanyId());
        if (timeshareCompany==null) throw new OptionalNotFoundException("Not found company");
        TimeshareCompanyStaff timeshareCompanyStaff = timeshareCompanyStaffRepository.findTimeshareCompanyStaffByUserNameAndTimeshareCompanyId(loginTSStaffRequestDto.getUsername(), loginTSStaffRequestDto.getTsCompanyId());
        if (timeshareCompanyStaff==null) throw new OptionalNotFoundException("Not found staff");
        if (timeshareCompanyStaff.getIsActive()==false) throw new AccountSuspendedException();
        if (!passwordEncoder.matches(loginTSStaffRequestDto.getPassword(), timeshareCompanyStaff.getPassword())) {
            throw new InvalidateException();
        }
        return timeshareCompanyStaff;
    }

    @Override
    public TimeShareCompanyStaffDTO getLoginStaff() {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            String jwtToken = (String) authentication.getCredentials();
            Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(jwtToken).getBody();
            Integer tsId = Integer.parseInt(claims.get("tsId").toString());
            username = claims.getSubject();
            TimeshareCompanyStaff timeshareCompanyStaff= timeshareCompanyStaffRepository.findTimeshareCompanyStaffByUserNameAndTimeshareCompanyId(username,tsId);
            TimeShareCompanyStaffDTO timeShareCompanyStaffDTO = timeshareCompanyStaffMapper.toDto(timeshareCompanyStaff);
            timeShareCompanyStaffDTO.setUserName(username);
            return timeShareCompanyStaffDTO;
        }
        return null;

    }
    private Key getSignInKey() {
        try {
            byte[] decodedKey = Decoders.BASE64.decode(SECRET_KEY);
            return Keys.hmacShaKeyFor(decodedKey);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error decoding secret key", e);
        }
    }
}
