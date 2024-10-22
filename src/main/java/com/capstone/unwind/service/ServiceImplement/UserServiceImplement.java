package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.Role;
import com.capstone.unwind.entity.User;
import com.capstone.unwind.exception.*;
import com.capstone.unwind.model.AuthDTO.RegisterRequestDTO;
import com.capstone.unwind.model.UserDTO.UpdateUserRequestDTO;
import com.capstone.unwind.model.UserDTO.UserDto;
import com.capstone.unwind.model.UserDTO.UserMapper;
import com.capstone.unwind.repository.RoleRepository;
import com.capstone.unwind.repository.TimeshareCompanyStaffRepository;
import com.capstone.unwind.repository.UserRepository;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
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

    @Autowired
    private TimeshareCompanyStaffRepository timeshareCompanyStaffRepository;

    @Override
    public UserDetails loadUserByUsername(String username, Integer tsId) {
        if (tsId == null) {
            return userRepository.findUserByUserName(username);
        } else {
            return timeshareCompanyStaffRepository.findTimeshareCompanyStaffByUserNameAndTimeshareCompanyId(username, tsId);
        }
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
}
