package com.rikkeibank.customer.dto;

import com.rikkeibank.customer.entity.CommonStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffRequest {

    private Long userId;

    @NotBlank(message = "Ho ten khong duoc de trong")
    private String fullName;

    private String department;
    private String phoneNumber;
    private String email;
    private CommonStatus status;
}
