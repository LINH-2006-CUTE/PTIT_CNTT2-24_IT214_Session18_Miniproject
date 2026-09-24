package com.rikkeibank.customer.repository;

import com.rikkeibank.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCifCode(String cifCode);
    Optional<Customer> findByIdentityCard(String identityCard);
    Optional<Customer> findByUserId(Long userId);
    boolean existsByCifCode(String cifCode);
    boolean existsByIdentityCard(String identityCard);
}
