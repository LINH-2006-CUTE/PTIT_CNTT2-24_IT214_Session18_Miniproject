package com.rikkeibank.transaction.client;

import com.rikkeibank.transaction.dto.*;
import org.springframework.stereotype.Component;

@Component
public class AccountClientFallback implements AccountClient {

    @Override
    public ApiResponse<AccountDto> getAccountByNumber(String accountNumber) {
        return ApiResponse.error("He thong tai khoan tam thoi khong kha dung (Fallback Circuit Breaker)");
    }

    @Override
    public ApiResponse<AccountDto> debit(DebitRequest request) {
        return ApiResponse.error("Khong the thuc hien tru tien do he thong tai khoan gap su co (Fallback Circuit Breaker)");
    }

    @Override
    public ApiResponse<AccountDto> credit(CreditRequest request) {
        return ApiResponse.error("Khong the thuc hien cong tien do he thong tai khoan gap su co (Fallback Circuit Breaker)");
    }

    @Override
    public ApiResponse<AccountDto> compensateCredit(CompensateRequest request) {
        return ApiResponse.error("Khong the thuc hien hoan tien do he thong tai khoan gap su co (Fallback Circuit Breaker)");
    }
}
