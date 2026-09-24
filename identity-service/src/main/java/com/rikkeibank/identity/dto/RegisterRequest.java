package com.rikkeibank.identity.dto;

import com.rikkeibank.identity.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Username khong duoc de trong")
    private String username;

    @NotBlank(message = "Password khong duoc de trong")
    private String password;

    @NotBlank(message = "Ho ten khong duoc de trong")
    private String fullName;

    private String email;
    private String phoneNumber;
    private Role role;
}
