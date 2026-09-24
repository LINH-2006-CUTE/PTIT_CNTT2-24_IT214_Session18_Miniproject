package com.rikkeibank.account.config;

import com.rikkeibank.account.entity.Account;
import com.rikkeibank.account.entity.AccountStatus;
import com.rikkeibank.account.entity.AccountType;
import com.rikkeibank.account.repository.AccountRepository;
import com.rikkeibank.account.repository.AccountTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountTypeRepository accountTypeRepository;
    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) {
        if (accountTypeRepository.count() == 0) {
            AccountType checking = accountTypeRepository.save(AccountType.builder()
                    .name("Tai khoan thanh toan")
                    .code("CHECKING")
                    .interestRate(BigDecimal.ZERO)
                    .description("Tai khoan thanh toan khong ky han")
                    .build());

            accountTypeRepository.save(AccountType.builder()
                    .name("Tai khoan tiet kiem")
                    .code("SAVINGS")
                    .interestRate(new BigDecimal("0.055"))
                    .description("Tai khoan tiet kiem co ky han sinh loi")
                    .build());

            if (accountRepository.count() == 0) {
                accountRepository.save(Account.builder()
                        .accountNumber("1001234567")
                        .customerId(1L)
                        .accountTypeId(checking.getId())
                        .balance(new BigDecimal("10000000.00"))
                        .currency("VND")
                        .status(AccountStatus.ACTIVE)
                        .build());

                accountRepository.save(Account.builder()
                        .accountNumber("1009876543")
                        .customerId(2L)
                        .accountTypeId(checking.getId())
                        .balance(new BigDecimal("5000000.00"))
                        .currency("VND")
                        .status(AccountStatus.ACTIVE)
                        .build());
            }
        }
    }
}
