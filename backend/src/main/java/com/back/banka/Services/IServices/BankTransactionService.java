package com.back.banka.Services.IServices;

import com.back.banka.Model.AccountBank;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankTransactionService {

    Optional<AccountBank> findByUserId(Long userId);
}
