package com.rikkeibank.account.service;

import com.rikkeibank.account.dto.*;
import com.rikkeibank.account.entity.Account;
import com.rikkeibank.account.entity.AccountStatus;
import com.rikkeibank.account.exception.ApiException;
import com.rikkeibank.account.repository.AccountRepository;
import com.rikkeibank.account.repository.AccountTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountTypeRepository accountTypeRepository;

    public AccountResponse createAccount(AccountRequest request) {
        if (!accountTypeRepository.existsById(request.getAccountTypeId())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay loai tai khoan voi ID: " + request.getAccountTypeId());
        }

        String accountNumber = generateUniqueAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .customerId(request.getCustomerId())
                .accountTypeId(request.getAccountTypeId())
                .balance(request.getInitialBalance() != null ? request.getInitialBalance() : BigDecimal.ZERO)
                .currency(request.getCurrency() != null ? request.getCurrency() : "VND")
                .status(AccountStatus.ACTIVE)
                .build();

        Account saved = accountRepository.save(account);
        return mapToResponse(saved);
    }

    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "accounts", key = "#accountNumber")
    public AccountResponse getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay tai khoan: " + accountNumber));
        return mapToResponse(account);
    }

    public List<AccountResponse> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "accounts", key = "#accountNumber")
    public AccountResponse updateStatus(String accountNumber, AccountStatus status) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay tai khoan: " + accountNumber));

        account.setStatus(status);
        Account saved = accountRepository.save(account);
        return mapToResponse(saved);
    }

    @Transactional
    @CachePut(value = "accounts", key = "#result.accountNumber")
    public AccountResponse debit(DebitRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay tai khoan nguon: " + request.getAccountNumber()));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Tai khoan nguon khong hoat dong hoac da bi khoa");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "So du khong du de thuc hien giao dich");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        Account saved = accountRepository.save(account);
        return mapToResponse(saved);
    }

    @Transactional
    @CachePut(value = "accounts", key = "#result.accountNumber")
    public AccountResponse credit(CreditRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay tai khoan dich: " + request.getAccountNumber()));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Tai khoan dich khong hoat dong hoac da bi khoa");
        }

        account.setBalance(account.getBalance().add(request.getAmount()));
        Account saved = accountRepository.save(account);
        return mapToResponse(saved);
    }

    @Transactional
    @CachePut(value = "accounts", key = "#result.accountNumber")
    public AccountResponse compensateCredit(CompensateRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay tai khoan de hoan tien: " + request.getAccountNumber()));

        account.setBalance(account.getBalance().add(request.getAmount()));
        Account saved = accountRepository.save(account);
        return mapToResponse(saved);
    }

    private String generateUniqueAccountNumber() {
        Random random = new Random();
        String number;
        do {
            number = "100" + (1000000 + random.nextInt(9000000));
        } while (accountRepository.existsByAccountNumber(number));
        return number;
    }

    public AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .customerId(account.getCustomerId())
                .accountTypeId(account.getAccountTypeId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .build();
    }
}
