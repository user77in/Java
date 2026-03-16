package com.shop.demo.service;

import com.shop.demo.dto.RegisterUserRequest;
import com.shop.demo.dto.UserResponse;
import com.shop.demo.exception.BadRequestException;
import com.shop.demo.exception.ResourceNotFoundException;
import com.shop.demo.model.User;
import com.shop.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponse registerUser(RegisterUserRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new BadRequestException("Name cannot be blank");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new BadRequestException("Email cannot be blank");
        }
        if (request.password() == null || request.password().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already registered :" + request.email());
        }
        User user = new User(request.name(), request.email(), request.password());
        return UserResponse.from( userRepository.save(user));
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found " + id));
        return UserResponse.from(user);
    }
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }
    public User findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }
}
