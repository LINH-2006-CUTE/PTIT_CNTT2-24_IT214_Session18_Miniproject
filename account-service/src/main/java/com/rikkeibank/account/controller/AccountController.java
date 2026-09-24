package com.rikkeibank.account.controller;

import com.rikkeibank.account.dto.*;
import com.rikkeibank.account.entity.AccountStatus;
import com.rikkeibank.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(ApiResponse.ok("Tao tai khoan moi thanh cong", response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllAccounts() {
        List<AccountResponse> list = accountService.getAllAccounts();
        return ResponseEntity.ok(ApiResponse.ok("Lay danh sach tai khoan thanh cong", list));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccountByNumber(@PathVariable String accountNumber) {
        AccountResponse response = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(ApiResponse.ok("Lay thong tin tai khoan thanh cong", response));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAccountsByCustomerId(@PathVariable Long customerId) {
        List<AccountResponse> list = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.ok("Lay danh sach tai khoan cua khach hang thanh cong", list));
    }

    @PutMapping("/{accountNumber}/status")
    public ResponseEntity<ApiResponse<AccountResponse>> updateStatus(@PathVariable String accountNumber,
                                                                     @RequestParam AccountStatus status) {
        AccountResponse response = accountService.updateStatus(accountNumber, status);
        return ResponseEntity.ok(ApiResponse.ok("Cap nhat trang thai tai khoan thanh cong", response));
    }

    @PostMapping("/debit")
    public ResponseEntity<ApiResponse<AccountResponse>> debit(@Valid @RequestBody DebitRequest request) {
        AccountResponse response = accountService.debit(request);
        return ResponseEntity.ok(ApiResponse.ok("Tru tien thanh cong", response));
    }

    @PostMapping("/credit")
    public ResponseEntity<ApiResponse<AccountResponse>> credit(@Valid @RequestBody CreditRequest request) {
        AccountResponse response = accountService.credit(request);
        return ResponseEntity.ok(ApiResponse.ok("Cong tien thanh cong", response));
    }

    @PostMapping("/compensate-credit")
    public ResponseEntity<ApiResponse<AccountResponse>> compensateCredit(@Valid @RequestBody CompensateRequest request) {
        AccountResponse response = accountService.compensateCredit(request);
        return ResponseEntity.ok(ApiResponse.ok("Hoan tien bu tru Saga thanh cong", response));
    }
}
