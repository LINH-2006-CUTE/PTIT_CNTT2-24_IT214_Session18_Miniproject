package com.rikkeibank.transaction.client;

import com.rikkeibank.transaction.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service", fallback = AccountClientFallback.class)
public interface AccountClient {

    @GetMapping("/api/accounts/{accountNumber}")
    ApiResponse<AccountDto> getAccountByNumber(@PathVariable("accountNumber") String accountNumber);

    @PostMapping("/api/accounts/debit")
    ApiResponse<AccountDto> debit(@RequestBody DebitRequest request);

    @PostMapping("/api/accounts/credit")
    ApiResponse<AccountDto> credit(@RequestBody CreditRequest request);

    @PostMapping("/api/accounts/compensate-credit")
    ApiResponse<AccountDto> compensateCredit(@RequestBody CompensateRequest request);
}
