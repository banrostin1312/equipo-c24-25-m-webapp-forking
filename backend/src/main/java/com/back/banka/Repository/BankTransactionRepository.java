package com.back.banka.Repository;

import com.back.banka.Enums.TransactionType;
import com.back.banka.Model.AccountBank;
import com.back.banka.Model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {

    @Query("SELECT t FROM BankTransaction t WHERE " +
    "(t.accountSend = :account OR t.accountReceiving = :account) " +
    "AND (:startDate IS NULL OR t.date >= :startDate) " +
    "AND (:endDate IS NULL OR t.date <= :endDate) " +
    "AND (:transactionType IS NULL OR t.transactionType = :transactionType) " +
    "ORDER BY t.date DESC")

    List<BankTransaction> findByAccountAndFilters(@Param("account") AccountBank accountBank,
                                                  @Param("startDate")LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate,
                                                  @Param("transactionType")TransactionType transactionType);

}




