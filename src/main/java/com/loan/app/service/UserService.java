package com.loan.app.service;

import com.loan.app.dto.UserDto;

public interface UserService {
    UserDto registerUser(UserDto userDto);
    UserDto getUserById(Long id);
    UserDto login(String email, String password);
    java.util.List<UserDto> getAllUsers();
}
