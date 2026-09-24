package com.rikkeibank.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
public class CompensateRequest {

    @NotBlank(message = "So tai khoan khong duoc de trong")
    private String accountNumber;

    @NotNull(message = "So tien khong duoc de trong")
    @DecimalMin(value = "0.01", message = "So tien phai lon hon 0")
    private BigDecimal amount;

    private String reason;
}
