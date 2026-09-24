package com.rikkeibank.identity.service;

import com.rikkeibank.identity.dto.UserResponse;
import com.rikkeibank.identity.entity.User;
import com.rikkeibank.identity.entity.UserStatus;
import com.rikkeibank.identity.exception.ApiException;
import com.rikkeibank.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public UserResponse getProfileByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay nguoi dung: " + username));
        return authService.mapToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(authService::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse updateStatus(Long id, UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay nguoi dung voi ID: " + id));
        user.setStatus(status);
        User savedUser = userRepository.save(user);
        return authService.mapToUserResponse(savedUser);
    }
}
