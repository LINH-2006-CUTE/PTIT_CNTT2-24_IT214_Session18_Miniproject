package com.rikkeibank.notification.service;

import com.rikkeibank.notification.dto.NotificationResponse;
import com.rikkeibank.notification.entity.Notification;
import com.rikkeibank.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getNotificationsByAccountNumber(String accountNumber) {
        return notificationRepository.findByRecipientAccountNumberOrderByCreatedAtDesc(accountNumber).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public NotificationResponse mapToResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .recipientAccountNumber(n.getRecipientAccountNumber())
                .title(n.getTitle())
                .content(n.getContent())
                .eventType(n.getEventType())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
