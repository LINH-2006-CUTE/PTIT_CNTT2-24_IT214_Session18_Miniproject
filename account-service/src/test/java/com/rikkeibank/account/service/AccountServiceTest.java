package com.rikkeibank.account.service;

import com.rikkeibank.account.dto.AccountResponse;
import com.rikkeibank.account.dto.CompensateRequest;
import com.rikkeibank.account.dto.CreditRequest;
import com.rikkeibank.account.dto.DebitRequest;
import com.rikkeibank.account.entity.Account;
import com.rikkeibank.account.entity.AccountStatus;
import com.rikkeibank.account.repository.AccountRepository;
import com.rikkeibank.account.repository.AccountTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountTypeRepository accountTypeRepository;

    @InjectMocks
    private AccountService accountService;

    private Account sampleAccount;

    @BeforeEach
    void setUp() {
        sampleAccount = Account.builder()
                .id(1L)
                .accountNumber("1001234567")
                .customerId(1L)
                .accountTypeId(1L)
                .balance(new BigDecimal("1000000.00"))
                .currency("VND")
                .status(AccountStatus.ACTIVE)
                .build();
    }

    @Test
    void testDebitSuccess() {
        DebitRequest request = DebitRequest.builder()
                .accountNumber("1001234567")
                .amount(new BigDecimal("200000.00"))
                .build();

        when(accountRepository.findByAccountNumber("1001234567")).thenReturn(Optional.of(sampleAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountResponse response = accountService.debit(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("800000.00"), response.getBalance());
        verify(accountRepository, times(1)).save(sampleAccount);
    }

    @Test
    void testCreditSuccess() {
        CreditRequest request = CreditRequest.builder()
                .accountNumber("1001234567")
                .amount(new BigDecimal("500000.00"))
                .build();

        when(accountRepository.findByAccountNumber("1001234567")).thenReturn(Optional.of(sampleAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountResponse response = accountService.credit(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("1500000.00"), response.getBalance());
        verify(accountRepository, times(1)).save(sampleAccount);
    }

    @Test
    void testCompensateCreditSuccess() {
        CompensateRequest request = CompensateRequest.builder()
                .accountNumber("1001234567")
                .amount(new BigDecimal("200000.00"))
                .reason("Rollback do loi cong tien")
                .build();

        when(accountRepository.findByAccountNumber("1001234567")).thenReturn(Optional.of(sampleAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountResponse response = accountService.compensateCredit(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("1200000.00"), response.getBalance());
        verify(accountRepository, times(1)).save(sampleAccount);
    }
}
