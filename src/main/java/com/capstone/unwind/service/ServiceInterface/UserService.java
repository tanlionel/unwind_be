package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.entity.User;
import com.capstone.unwind.exception.AccountSuspendedException;
import com.capstone.unwind.exception.InvalidateException;
import com.capstone.unwind.exception.UserDoesNotExistException;
import com.capstone.unwind.model.AuthDTO.RegisterRequestDTO;
import com.capstone.unwind.model.UserDTO.UpdateUserRequestDTO;
import com.capstone.unwind.model.UserDTO.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User login(String email, String password) throws UserDoesNotExistException, AccountSuspendedException, InvalidateException;
    User registerUser(RegisterRequestDTO registerUser) throws Exception;
    User getLoginUser();
    User getUserByUserName(String Username) throws UserDoesNotExistException, InvalidateException;
    Page<UserDto> getPageableUser(Integer pageNo, Integer pageSize, String userName, Integer roleId);
    UserDto getUserByUserId(Integer userId) throws UserDoesNotExistException;
    UserDto createUser(RegisterRequestDTO registerRequestDTO) throws Exception;
    UserDto updateUser(Integer userId, UpdateUserRequestDTO updateUserRequestDTO) throws Exception;
}
