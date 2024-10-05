package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.Role;
import com.capstone.unwind.entity.User;
import com.capstone.unwind.exception.*;
import com.capstone.unwind.model.AuthDTO.RegisterRequestDTO;
import com.capstone.unwind.repository.RoleRepository;
import com.capstone.unwind.repository.UserRepository;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
        boolean isExistUser = userRepository.findUserByEmail(registerUser.getEmail()) != null;

        Optional<Role> existRole = roleRepository.findById(registerUser.getRoleId());
        if (existRole.isEmpty()) throw new RoleDoesNotExistException();
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
}
