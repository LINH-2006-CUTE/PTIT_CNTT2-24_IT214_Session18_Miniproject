package com.rikkeibank.transaction.service;

import com.rikkeibank.transaction.client.AccountClient;
import com.rikkeibank.transaction.dto.AccountDto;
import com.rikkeibank.transaction.dto.ApiResponse;
import com.rikkeibank.transaction.dto.TransactionResponse;
import com.rikkeibank.transaction.dto.TransferRequest;
import com.rikkeibank.transaction.entity.Transaction;
import com.rikkeibank.transaction.entity.TransactionStatus;
import com.rikkeibank.transaction.event.EventPublisher;
import com.rikkeibank.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountClient accountClient;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testProcessTransferSuccess() {
        TransferRequest request = TransferRequest.builder()
                .sourceAccountNumber("1001234567")
                .targetAccountNumber("1009876543")
                .amount(new BigDecimal("500000.00"))
                .description("Chuyen tien binh thuong")
                .build();

        AccountDto dummyAccount = AccountDto.builder().accountNumber("1001234567").build();
        when(accountClient.debit(any())).thenReturn(ApiResponse.ok(dummyAccount));
        when(accountClient.credit(any())).thenReturn(ApiResponse.ok(dummyAccount));

        TransactionResponse response = transactionService.processTransfer(request, "tester");

        assertNotNull(response);
        assertEquals(TransactionStatus.SUCCESS, response.getStatus());
        verify(accountClient, times(1)).debit(any());
        verify(accountClient, times(1)).credit(any());
        verify(accountClient, never()).compensateCredit(any());
        verify(eventPublisher, times(1)).publishTransactionEvent(any());
    }

    @Test
    void testProcessTransferRollbackSimulation() {
        TransferRequest request = TransferRequest.builder()
                .sourceAccountNumber("1001234567")
                .targetAccountNumber("1009876543")
                .amount(new BigDecimal("100000.00"))
                .description("Chuyen tien SIMULATE_ROLLBACK kiem tra")
                .build();

        AccountDto dummyAccount = AccountDto.builder().accountNumber("1001234567").build();
        when(accountClient.debit(any())).thenReturn(ApiResponse.ok(dummyAccount));
        when(accountClient.compensateCredit(any())).thenReturn(ApiResponse.ok(dummyAccount));

        TransactionResponse response = transactionService.processTransfer(request, "tester");

        assertNotNull(response);
        assertEquals(TransactionStatus.COMPENSATED, response.getStatus());
        verify(accountClient, times(1)).debit(any());
        verify(accountClient, times(1)).compensateCredit(any());
        verify(eventPublisher, times(1)).publishTransactionEvent(any());
    }
}
