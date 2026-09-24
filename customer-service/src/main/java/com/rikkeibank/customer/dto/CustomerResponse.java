package com.rikkeibank.customer.dto;

import com.rikkeibank.customer.entity.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long id;
    private Long userId;
    private String cifCode;
    private String fullName;
    private String identityCard;
    private String phoneNumber;
    private String email;
    private String address;
    private CommonStatus status;
    private LocalDateTime createdAt;
}
