package com.capstone.unwind.controller;

import com.capstone.unwind.entity.User;
import com.capstone.unwind.exception.UserDoesNotExistException;
import com.capstone.unwind.model.AuthDTO.BasicUserResponseDTO;
import com.capstone.unwind.model.AuthDTO.RegisterRequestDTO;
import com.capstone.unwind.model.UserDTO.UpdateUserRequestDTO;
import com.capstone.unwind.model.UserDTO.UserDto;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@CrossOrigin
public class AdminUserController {
    @Autowired
    private final UserService userService;

    @GetMapping()
    private Page<UserDto> getPageableUser(@RequestParam(required = false,defaultValue = "0") Integer pageNo,
                                          @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                          @RequestParam(required = false,defaultValue = "") String userName,
                                          @RequestParam(required = false) Integer roleId){
        return userService.getPageableUser(pageNo,pageSize,userName,roleId);
    }
    @GetMapping("{userId}")
    private UserDto getUserByUserId(@PathVariable Integer userId) throws UserDoesNotExistException {
        return userService.getUserByUserId(userId);
    }
    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody RegisterRequestDTO registeredUser) throws Exception{
        UserDto user = userService.createUser(registeredUser);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Integer userId, @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
        try {
            UserDto updatedUser = userService.updateUser(userId, updateUserRequestDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/timeshare-company")
    public ResponseEntity<List<UserDto>> getAllTimeshareCompanyAccount(){
        List<UserDto> userDtoList = userService.getAllTimeshareCompanyAccount();
        return ResponseEntity.ok(userDtoList);
    }
}
