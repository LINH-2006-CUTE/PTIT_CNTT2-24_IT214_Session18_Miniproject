package com.rikkeibank.transaction.service;

import com.rikkeibank.transaction.client.AccountClient;
import com.rikkeibank.transaction.dto.*;
import com.rikkeibank.transaction.entity.Transaction;
import com.rikkeibank.transaction.entity.TransactionStatus;
import com.rikkeibank.transaction.entity.TransactionType;
import com.rikkeibank.transaction.event.EventPublisher;
import com.rikkeibank.transaction.exception.ApiException;
import com.rikkeibank.transaction.repository.TransactionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;
    private final EventPublisher eventPublisher;

    @CircuitBreaker(name = "accountService", fallbackMethod = "transferFallback")
    public TransactionResponse processTransfer(TransferRequest request, String performedBy) {
        if (request.getSourceAccountNumber().equals(request.getTargetAccountNumber())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Tai khoan nguon va tai khoan dich khong duoc giong nhau");
        }

        String transactionCode = "TXN" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();

        Transaction transaction = Transaction.builder()
                .transactionCode(transactionCode)
                .sourceAccountNumber(request.getSourceAccountNumber())
                .targetAccountNumber(request.getTargetAccountNumber())
                .amount(request.getAmount())
                .currency("VND")
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .description(request.getDescription())
                .performedBy(performedBy != null ? performedBy : "SYSTEM")
                .build();

        Transaction savedTx = transactionRepository.save(transaction);

        DebitRequest debitReq = DebitRequest.builder()
                .accountNumber(request.getSourceAccountNumber())
                .amount(request.getAmount())
                .build();

        ApiResponse<AccountDto> debitResp;
        try {
            debitResp = accountClient.debit(debitReq);
        } catch (Exception e) {
            savedTx.setStatus(TransactionStatus.FAILED);
            savedTx.setFailureReason("Loi goi service tai khoan: " + e.getMessage());
            transactionRepository.save(savedTx);
            throw new ApiException(HttpStatus.BAD_REQUEST, savedTx.getFailureReason());
        }

        if (debitResp == null || !debitResp.isSuccess()) {
            String errorMsg = debitResp != null ? debitResp.getMessage() : "Tru tien that bai tu tai khoan nguon";
            savedTx.setStatus(TransactionStatus.FAILED);
            savedTx.setFailureReason(errorMsg);
            transactionRepository.save(savedTx);
            throw new ApiException(HttpStatus.BAD_REQUEST, errorMsg);
        }

        boolean simulateFailure = request.getDescription() != null && request.getDescription().contains("SIMULATE_ROLLBACK");

        if (simulateFailure) {
            CompensateRequest compReq = CompensateRequest.builder()
                    .accountNumber(request.getSourceAccountNumber())
                    .amount(request.getAmount())
                    .reason("Mo phong loi de thuc hien Saga Rollback")
                    .build();
            accountClient.compensateCredit(compReq);

            savedTx.setStatus(TransactionStatus.COMPENSATED);
            savedTx.setFailureReason("Kich ban mo phong loi thanh cong: Tien da duoc hoan lai tai khoan nguon (Saga Compensating)");
            Transaction compensatedTx = transactionRepository.save(savedTx);

            publishEvent(compensatedTx, "TRANSFER_COMPENSATED");
            return mapToResponse(compensatedTx);
        }

        CreditRequest creditReq = CreditRequest.builder()
                .accountNumber(request.getTargetAccountNumber())
                .amount(request.getAmount())
                .build();

        ApiResponse<AccountDto> creditResp;
        try {
            creditResp = accountClient.credit(creditReq);
        } catch (Exception e) {
            creditResp = ApiResponse.error(e.getMessage());
        }

        if (creditResp == null || !creditResp.isSuccess()) {
            CompensateRequest compReq = CompensateRequest.builder()
                    .accountNumber(request.getSourceAccountNumber())
                    .amount(request.getAmount())
                    .reason("Loi cong tien vao tai khoan dich: " + (creditResp != null ? creditResp.getMessage() : ""))
                    .build();
            accountClient.compensateCredit(compReq);

            savedTx.setStatus(TransactionStatus.COMPENSATED);
            savedTx.setFailureReason("Cong tien vao tai khoan dich that bai. Da boi hoan Saga thanh cong: " + (creditResp != null ? creditResp.getMessage() : ""));
            Transaction compensatedTx = transactionRepository.save(savedTx);

            publishEvent(compensatedTx, "TRANSFER_COMPENSATED");
            return mapToResponse(compensatedTx);
        }

        savedTx.setStatus(TransactionStatus.SUCCESS);
        Transaction completedTx = transactionRepository.save(savedTx);

        publishEvent(completedTx, "TRANSFER_SUCCESS");
        return mapToResponse(completedTx);
    }

    public TransactionResponse transferFallback(TransferRequest request, String performedBy, Throwable throwable) {
        throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "Dich vu tai khoan tam thoi khong kha dung. Circuit Breaker da ngat ket noi de bao ve he thong: " + throwable.getMessage());
    }

    public List<TransactionResponse> getMyTransactions(String accountNumber) {
        return transactionRepository.findBySourceAccountNumberOrTargetAccountNumber(accountNumber, accountNumber).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getDailyTransactions() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        return transactionRepository.findDailyTransactions(startOfDay, endOfDay).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TransactionResponse getTransactionById(Long id) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay giao dich voi ID: " + id));
        return mapToResponse(tx);
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void publishEvent(Transaction tx, String eventType) {
        TransactionEvent event = TransactionEvent.builder()
                .eventType(eventType)
                .transactionCode(tx.getTransactionCode())
                .sourceAccountNumber(tx.getSourceAccountNumber())
                .targetAccountNumber(tx.getTargetAccountNumber())
                .amount(tx.getAmount())
                .currency(tx.getCurrency())
                .status(tx.getStatus().name())
                .description(tx.getDescription())
                .timestamp(LocalDateTime.now())
                .build();
        eventPublisher.publishTransactionEvent(event);
    }

    public TransactionResponse mapToResponse(Transaction tx) {
        return TransactionResponse.builder()
                .id(tx.getId())
                .transactionCode(tx.getTransactionCode())
                .sourceAccountNumber(tx.getSourceAccountNumber())
                .targetAccountNumber(tx.getTargetAccountNumber())
                .amount(tx.getAmount())
                .currency(tx.getCurrency())
                .type(tx.getType())
                .status(tx.getStatus())
                .description(tx.getDescription())
                .performedBy(tx.getPerformedBy())
                .failureReason(tx.getFailureReason())
                .createdAt(tx.getCreatedAt())
                .build();
    }
}
