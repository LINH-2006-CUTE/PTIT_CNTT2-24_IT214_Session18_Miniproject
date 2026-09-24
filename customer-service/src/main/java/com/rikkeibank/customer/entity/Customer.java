package com.rikkeibank.customer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(unique = true, nullable = false)
    private String cifCode;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String identityCard;

    private String phoneNumber;

    private String email;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommonStatus status;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = CommonStatus.ACTIVE;
        }
    }
}
