package com.rikkeibank.account.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTypeRequest {

    @NotBlank(message = "Ten loai tai khoan khong duoc de trong")
    private String name;

    @NotBlank(message = "Ma loai tai khoan khong duoc de trong")
    private String code;

    private BigDecimal interestRate;
    private String description;
}
