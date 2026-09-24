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
public class CustomerRequest {

    private Long userId;

    @NotBlank(message = "Ho ten khong duoc de trong")
    private String fullName;

    @NotBlank(message = "Can cuoc cong dan khong duoc de trong")
    private String identityCard;

    private String phoneNumber;
    private String email;
    private String address;
    private CommonStatus status;
}
