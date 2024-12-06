package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.Role;
import com.capstone.unwind.entity.TimeshareCompanyStaff;
import com.capstone.unwind.entity.User;
import com.capstone.unwind.enums.EmailEnum;
import com.capstone.unwind.exception.*;
import com.capstone.unwind.model.AuthDTO.RegisterRequestDTO;
import com.capstone.unwind.model.EmailRequestDTO.EmailRequestDto;
import com.capstone.unwind.model.UserDTO.UpdateUserRequestDTO;
import com.capstone.unwind.model.UserDTO.UserDto;
import com.capstone.unwind.model.UserDTO.UserMapper;
import com.capstone.unwind.repository.RoleRepository;
import com.capstone.unwind.repository.TimeshareCompanyStaffRepository;
import com.capstone.unwind.repository.UserRepository;
import com.capstone.unwind.service.ServiceInterface.JwtService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.capstone.unwind.config.EmailMessageConfig.RESORT_CREATION_CONTENT;
import static com.capstone.unwind.config.EmailMessageConfig.RESORT_CREATION_SUBJECT;

@RequiredArgsConstructor
@Service
public class UserServiceImplement implements UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private final SendinblueService sendinblueService;
    @Autowired
    private final TimeshareCompanyStaffRepository timeshareCompanyStaffRepository;

    @Override
    public User login(String email, String password) throws UserDoesNotExistException, AccountSuspendedException, InvalidateException {
        User loginUser = userRepository.findUserByEmail(email);
        if (loginUser == null) throw new UserDoesNotExistException();
        if (!loginUser.getIsActive()) throw new AccountSuspendedException();
        if (!passwordEncoder.matches(password, loginUser.getPassword())) {
            throw new InvalidateException();
        }
        return loginUser;
    }

    @Override
    public User registerUser(RegisterRequestDTO registerUser) throws Exception {
        if (registerUser.getRoleId() != 1 ) throw new RoleDoesNotAcceptException();
        boolean isExistUser = userRepository.findUserByEmail(registerUser.getEmail()) != null;
        Optional<Role> existRole = roleRepository.findById(registerUser.getRoleId());
        if (existRole.isEmpty()) throw new RoleDoesNotAcceptException();
        Role role = existRole.get();
        if (isExistUser) throw new UserAlreadyExistsException();
        boolean isExistUserName = userRepository.findUserByUserName(registerUser.getUsername()) != null;
        if (isExistUserName) throw  new UserAlreadyExistsException();

        User user = User.builder()
                .userName(registerUser.getUsername())
                .email(registerUser.getEmail())
                .password(passwordEncoder.encode(registerUser.getPassword()).trim())
                .isActive(true)
                .role(role)
                .build();

        User result = userRepository.save(user);
        return result;
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userRepository.findUserByUserName(username);
        }
        return null;
    }

    @Override
    public User getUserByUserName(String Username) throws UserDoesNotExistException, InvalidateException {
        User user = userRepository.findUserByUserName(Username);
        if (user == null) {
            throw new UserDoesNotExistException();
        }
        return user;
    }

    @Override
    public Page<UserDto> getPageableUser(Integer pageNo, Integer pageSize, String userName, Integer roleId) {
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("id").ascending());
        Page<User> users = userRepository.findAllByUserNameContainingAndRoleId(userName,roleId,pageable);
        Page<UserDto> userDtos = users.map(userMapper::toDto);
        return userDtos;
    }

    @Override
    public UserDto getUserByUserId(Integer userId) throws UserDoesNotExistException {
        User user = userRepository.findUserById(userId);
        if (user==null) throw new UserDoesNotExistException();
        UserDto userDto = userMapper.toDto(user);
        return userDto;
    }

    @Override
    public UserDto createUser(RegisterRequestDTO registerRequestDTO) throws Exception {
        boolean isExistUser = userRepository.findUserByEmail(registerRequestDTO.getEmail()) != null;
        Optional<Role> existRole = roleRepository.findById(registerRequestDTO.getRoleId());
        if (existRole.isEmpty()) throw new RoleDoesNotAcceptException();
        Role role = existRole.get();
        if (isExistUser) throw new UserAlreadyExistsException();
        boolean isExistUserName = userRepository.findUserByUserName(registerRequestDTO.getUsername()) != null;
        if (isExistUserName) throw  new UserAlreadyExistsException();

        User user = User.builder()
                .userName(registerRequestDTO.getUsername())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()).trim())
                .isActive(true)
                .role(role)
                .build();

        User result = userRepository.save(user);
        UserDto userDto = userMapper.toDto(result);
        return userDto;
    }
    @Override
    public UserDto updateUser(Integer userId, UpdateUserRequestDTO updateUserRequestDTO) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException());
        Optional<Role> existRole = roleRepository.findById(updateUserRequestDTO.getRoleId());
        if (existRole.isEmpty()) throw new RoleDoesNotAcceptException();
        user.setRole(existRole.get());
        user.setIsActive(updateUserRequestDTO.getIsActive());

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public List<UserDto> getAllTimeshareCompanyAccount() {
        Integer timeshareCompanyRoleId = 2;
        List<User> users = userRepository.findAllByRoleId(timeshareCompanyRoleId);
        List<UserDto> userDtoList = users.stream().map(userMapper::toDto).toList();
        return userDtoList;
    }
    @Override
    public String getTokenForgot(String email) throws ErrMessageException {
        User user = userRepository.findUserByEmail(email);
                if (user == null) throw new ErrMessageException( "Email not found");

        // Random 6 ký tự
        String token = RandomStringUtils.randomNumeric(6);
        user.setForgotPwdToken(token);


        // Set thời gian hết hạn (10 phút sau)
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);
        user.setExpiredPwdToken(expiryTime);

        userRepository.save(user);

        try {
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setSubject("Mã token đặt lại mật khẩu của bạn");
            emailRequestDto.setContent(token);
            sendinblueService.sendEmailWithTemplate(
                    email,
                    EmailEnum.BASIC_MAIL,
                    emailRequestDto
            );
        } catch (Exception e) {
            throw new ErrMessageException("Failed to send email notification");
        }
        return token;
    }

    @Override
    public void  resetPassword(String email,
                                String token,
                                String newPassword) throws ErrMessageException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) throw new ErrMessageException( "Email not found");

        if (!token.equals(user.getForgotPwdToken())) {
            throw new ErrMessageException("Invalid token");
        }

        if (user.getExpiredPwdToken() == null || LocalDateTime.now().isAfter(user.getExpiredPwdToken())) {
            throw new ErrMessageException( "Token expired");
        }

        // Đặt mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword).trim());
        user.setForgotPwdToken(null);
        user.setExpiredPwdToken(null);

        try {
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setSubject("Mật khẩu của mới của bạn đã được dặt thành công");
            emailRequestDto.setContent(newPassword);
            sendinblueService.sendEmailWithTemplate(
                    email,
                    EmailEnum.BASIC_MAIL,
                    emailRequestDto
            );
        } catch (Exception e) {
            throw new ErrMessageException("Failed to send email notification");
        }

        userRepository.save(user);

    }
}
