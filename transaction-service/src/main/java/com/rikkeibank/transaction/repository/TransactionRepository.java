package com.rikkeibank.transaction.repository;

import com.rikkeibank.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionCode(String transactionCode);

    List<Transaction> findBySourceAccountNumberOrTargetAccountNumber(String source, String target);

    @Query("SELECT t FROM Transaction t WHERE t.createdAt >= :startOfDay AND t.createdAt <= :endOfDay ORDER BY t.createdAt DESC")
    List<Transaction> findDailyTransactions(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
