package com.loan.app.service.impl;

import com.loan.app.dto.UserDto;
import com.loan.app.entity.User;
import com.loan.app.exception.ResourceNotFoundException;
import com.loan.app.repository.UserRepository;
import com.loan.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto registerUser(UserDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email is already taken!");
        }
        User user = modelMapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Basic password check for the assignment. In real-world, use BCryptPasswordEncoder.
        if(!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password!");
        }
        
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public java.util.List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(java.util.stream.Collectors.toList());
    }
}
