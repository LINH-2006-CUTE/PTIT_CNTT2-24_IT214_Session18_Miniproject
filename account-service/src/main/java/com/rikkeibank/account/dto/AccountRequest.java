package com.rikkeibank.account.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {

    @NotNull(message = "Customer ID khong duoc de trong")
    private Long customerId;

    @NotNull(message = "Account Type ID khong duoc de trong")
    private Long accountTypeId;

    private BigDecimal initialBalance;
    private String currency;
}
