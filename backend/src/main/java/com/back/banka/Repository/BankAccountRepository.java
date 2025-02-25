package com.back.banka.Repository;

import com.back.banka.Model.AccountBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository  extends JpaRepository<AccountBank, Long> {
}
