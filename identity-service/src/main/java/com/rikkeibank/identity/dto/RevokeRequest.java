package com.rikkeibank.identity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevokeRequest {

    @NotBlank(message = "Username can thu hoi khong duoc de trong")
    private String username;

    private String reason;
    private boolean blockUser;
}
