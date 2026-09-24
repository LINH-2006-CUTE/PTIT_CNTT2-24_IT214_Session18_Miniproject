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
public class StaffResponse {

    private Long id;
    private Long userId;
    private String staffCode;
    private String fullName;
    private String department;
    private String phoneNumber;
    private String email;
    private CommonStatus status;
    private LocalDateTime createdAt;
}
