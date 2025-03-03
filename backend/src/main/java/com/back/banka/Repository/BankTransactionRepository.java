package com.back.banka.Repository;

import com.back.banka.Model.AccountBank;
import com.back.banka.Model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {

    @Query("SELECT t FROM BankTransaction t WHERE t.accountSend = :account OR t.accountReceiving = :account ORDER BY t.date DESC")

    List<BankTransaction> findTransactionsByAccount(AccountBank account);

}




