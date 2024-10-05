package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.entity.User;
import com.capstone.unwind.exception.AccountSuspendedException;
import com.capstone.unwind.exception.InvalidateException;
import com.capstone.unwind.exception.UserDoesNotExistException;
import com.capstone.unwind.model.AuthDTO.RegisterRequestDTO;

public interface UserService {
    User login(String email, String password) throws UserDoesNotExistException, AccountSuspendedException, InvalidateException;
    User registerUser(RegisterRequestDTO registerUser) throws Exception;
    User getLoginUser();
    User getUserByUserName(String Username) throws UserDoesNotExistException, InvalidateException;
}
