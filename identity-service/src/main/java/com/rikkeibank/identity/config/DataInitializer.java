package com.rikkeibank.identity.config;

import com.rikkeibank.identity.entity.Role;
import com.rikkeibank.identity.entity.User;
import com.rikkeibank.identity.entity.UserStatus;
import com.rikkeibank.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Quan Tri Vien")
                    .email("admin@rikkeibank.com")
                    .phoneNumber("0988000111")
                    .role(Role.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .build());

            userRepository.save(User.builder()
                    .username("teller")
                    .password(passwordEncoder.encode("teller123"))
                    .fullName("Giao Dich Vien")
                    .email("teller@rikkeibank.com")
                    .phoneNumber("0988000222")
                    .role(Role.TELLER)
                    .status(UserStatus.ACTIVE)
                    .build());

            userRepository.save(User.builder()
                    .username("customer1")
                    .password(passwordEncoder.encode("customer123"))
                    .fullName("Nguyen Van A")
                    .email("nguyenvana@gmail.com")
                    .phoneNumber("0988000333")
                    .role(Role.CUSTOMER)
                    .status(UserStatus.ACTIVE)
                    .build());

            userRepository.save(User.builder()
                    .username("customer2")
                    .password(passwordEncoder.encode("customer123"))
                    .fullName("Tran Thi B")
                    .email("tranthib@gmail.com")
                    .phoneNumber("0988000444")
                    .role(Role.CUSTOMER)
                    .status(UserStatus.ACTIVE)
                    .build());
        }
    }
}
