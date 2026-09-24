package com.rikkeibank.account.controller;

import com.rikkeibank.account.dto.AccountTypeRequest;
import com.rikkeibank.account.dto.AccountTypeResponse;
import com.rikkeibank.account.dto.ApiResponse;
import com.rikkeibank.account.service.AccountTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-types")
@RequiredArgsConstructor
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountTypeResponse>> createAccountType(@Valid @RequestBody AccountTypeRequest request) {
        AccountTypeResponse response = accountTypeService.createAccountType(request);
        return new ResponseEntity<>(ApiResponse.ok("Tao loai tai khoan thanh cong", response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountTypeResponse>>> getAllAccountTypes() {
        List<AccountTypeResponse> list = accountTypeService.getAllAccountTypes();
        return ResponseEntity.ok(ApiResponse.ok("Lay danh sach loai tai khoan thanh cong", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountTypeResponse>> getAccountTypeById(@PathVariable Long id) {
        AccountTypeResponse response = accountTypeService.getAccountTypeById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lay thong tin loai tai khoan thanh cong", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountTypeResponse>> updateAccountType(@PathVariable Long id,
                                                                             @Valid @RequestBody AccountTypeRequest request) {
        AccountTypeResponse response = accountTypeService.updateAccountType(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cap nhat loai tai khoan thanh cong", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAccountType(@PathVariable Long id) {
        accountTypeService.deleteAccountType(id);
        return ResponseEntity.ok(ApiResponse.ok("Xoa loai tai khoan thanh cong", null));
    }
}
