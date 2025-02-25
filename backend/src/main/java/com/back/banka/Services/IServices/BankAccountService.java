package com.back.banka.Services.IServices;

import com.back.banka.Dtos.ResponseDto.ActiveAccountDto;

import java.math.BigDecimal;

public interface BankAccountService {

    ActiveAccountDto activeAccount(Integer accountNumber);
    ActiveAccountDto deactivateAccount(Integer accountNumber);
    ActiveAccountDto getBalance(Long accountId);


}
