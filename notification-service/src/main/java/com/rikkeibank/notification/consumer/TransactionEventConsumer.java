package com.rikkeibank.notification.consumer;

import com.rikkeibank.notification.dto.TransactionEvent;
import com.rikkeibank.notification.entity.Notification;
import com.rikkeibank.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionEventConsumer {

    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "transaction-events", groupId = "notification-group")
    public void consumeTransactionEvent(TransactionEvent event) {
        if ("TRANSFER_SUCCESS".equals(event.getEventType())) {
            Notification debitNotification = Notification.builder()
                    .recipientAccountNumber(event.getSourceAccountNumber())
                    .title("Bien dong so du: Tru tien")
                    .content(String.format("Tai khoan %s bi tru %s %s chuyen toi tai khoan %s. Ma GD: %s",
                            event.getSourceAccountNumber(), event.getAmount(), event.getCurrency(),
                            event.getTargetAccountNumber(), event.getTransactionCode()))
                    .eventType(event.getEventType())
                    .build();
            notificationRepository.save(debitNotification);

            Notification creditNotification = Notification.builder()
                    .recipientAccountNumber(event.getTargetAccountNumber())
                    .title("Bien dong so du: Cong tien")
                    .content(String.format("Tai khoan %s nhan duoc %s %s tu tai khoan %s. Ma GD: %s",
                            event.getTargetAccountNumber(), event.getAmount(), event.getCurrency(),
                            event.getSourceAccountNumber(), event.getTransactionCode()))
                    .eventType(event.getEventType())
                    .build();
            notificationRepository.save(creditNotification);
        } else if ("TRANSFER_COMPENSATED".equals(event.getEventType())) {
            Notification rollbackNotification = Notification.builder()
                    .recipientAccountNumber(event.getSourceAccountNumber())
                    .title("Thong bao boi hoan giao dich (Saga Rollback)")
                    .content(String.format("Giao dich %s chuyen toi tai khoan %s gap loi. He thong da hoan tra %s %s vao tai khoan cua ban.",
                            event.getTransactionCode(), event.getTargetAccountNumber(),
                            event.getAmount(), event.getCurrency()))
                    .eventType(event.getEventType())
                    .build();
            notificationRepository.save(rollbackNotification);
        }
    }
}
