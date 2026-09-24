package com.rikkeibank.transaction.controller;

import com.rikkeibank.transaction.dto.ApiResponse;
import com.rikkeibank.transaction.dto.TransactionResponse;
import com.rikkeibank.transaction.dto.TransferRequest;
import com.rikkeibank.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(@Valid @RequestBody TransferRequest request,
                                                                     @RequestHeader(value = "X-User-Name", required = false) String performedBy) {
        TransactionResponse response = transactionService.processTransfer(request, performedBy);
        return new ResponseEntity<>(ApiResponse.ok("Xu ly giao dich chuyen khoan hoan tat", response), HttpStatus.CREATED);
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getMyTransactions(@RequestParam String accountNumber) {
        List<TransactionResponse> list = transactionService.getMyTransactions(accountNumber);
        return ResponseEntity.ok(ApiResponse.ok("Lay danh sach giao dich cua tai khoan thanh cong", list));
    }

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getDailyTransactions() {
        List<TransactionResponse> list = transactionService.getDailyTransactions();
        return ResponseEntity.ok(ApiResponse.ok("Lay danh sach giao dich trong ngay thanh cong", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransactionById(@PathVariable Long id) {
        TransactionResponse response = transactionService.getTransactionById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lay chi tiet giao dich thanh cong", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAllTransactions() {
        List<TransactionResponse> list = transactionService.getAllTransactions();
        return ResponseEntity.ok(ApiResponse.ok("Lay toan bo danh sach giao dich thanh cong", list));
    }
}
