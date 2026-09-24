package com.rikkeibank.transaction.event;

import com.rikkeibank.transaction.dto.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "transaction-events";

    public void publishTransactionEvent(TransactionEvent event) {
        try {
            kafkaTemplate.send(TOPIC, event.getTransactionCode(), event);
        } catch (Exception ignored) {
        }
    }
}
