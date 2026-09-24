package com.rikkeibank.identity.controller;

import com.rikkeibank.identity.dto.ApiResponse;
import com.rikkeibank.identity.dto.UserResponse;
import com.rikkeibank.identity.entity.UserStatus;
import com.rikkeibank.identity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@RequestHeader(value = "X-User-Name", required = false) String username,
                                                                 @RequestParam(value = "username", required = false) String paramUsername) {
        String effectiveUsername = username != null && !username.isBlank() ? username : paramUsername;
        if (effectiveUsername == null || effectiveUsername.isBlank()) {
            effectiveUsername = "admin";
        }
        UserResponse response = userService.getProfileByUsername(effectiveUsername);
        return ResponseEntity.ok(ApiResponse.ok("Lay thong tin nguoi dung thanh cong", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> list = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok("Lay danh sach nguoi dung thanh cong", list));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<UserResponse>> updateStatus(@PathVariable Long id,
                                                                 @RequestParam UserStatus status) {
        UserResponse response = userService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Cap nhat trang thai nguoi dung thanh cong", response));
    }
}
