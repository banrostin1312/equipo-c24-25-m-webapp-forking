package com.back.banka.Services.IServices;

import com.back.banka.Model.AccountBank;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankTransactionService {

    Optional<AccountBank> findByUserId(Long userId);
}
