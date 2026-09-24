package com.rikkeibank.notification.controller;

import com.rikkeibank.notification.dto.ApiResponse;
import com.rikkeibank.notification.dto.NotificationResponse;
import com.rikkeibank.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getAllNotifications() {
        List<NotificationResponse> list = notificationService.getAllNotifications();
        return ResponseEntity.ok(ApiResponse.ok("Lay danh sach thong bao thanh cong", list));
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getByAccountNumber(@PathVariable String accountNumber) {
        List<NotificationResponse> list = notificationService.getNotificationsByAccountNumber(accountNumber);
        return ResponseEntity.ok(ApiResponse.ok("Lay danh sach thong bao cua tai khoan thanh cong", list));
    }
}
