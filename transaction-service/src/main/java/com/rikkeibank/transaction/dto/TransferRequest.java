package com.rikkeibank.transaction.dto;

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
public class TransferRequest {

    @NotBlank(message = "Tai khoan nguon khong duoc de trong")
    private String sourceAccountNumber;

    @NotBlank(message = "Tai khoan dich khong duoc de trong")
    private String targetAccountNumber;

    @NotNull(message = "So tien chuyen khong duoc de trong")
    @DecimalMin(value = "0.01", message = "So tien chuyen phai lon hon 0")
    private BigDecimal amount;

    private String description;
}
