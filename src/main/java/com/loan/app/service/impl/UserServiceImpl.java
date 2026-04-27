package com.loan.app.service.impl;

import com.loan.app.dto.UserDto;
import com.loan.app.entity.User;
import com.loan.app.entity.Role;
import com.loan.app.entity.UserStatus;
import com.loan.app.exception.ResourceNotFoundException;
import com.loan.app.repository.UserRepository;
import com.loan.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }
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
        
        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new IllegalArgumentException("Account is blocked. Please contact admin.");
        }

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

    @Override
    @Transactional
    public UserDto updateUserRole(Long id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        user.setRole(Role.valueOf(role.toUpperCase()));
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    @Transactional
    public UserDto updateUserStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        user.setStatus(UserStatus.valueOf(status.toUpperCase()));
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
