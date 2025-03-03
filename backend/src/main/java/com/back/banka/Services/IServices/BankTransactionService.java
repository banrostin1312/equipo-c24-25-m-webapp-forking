package com.back.banka.Services.IServices;


import com.back.banka.Dtos.ResponseDto.SendMoneyResponseDto;
import com.back.banka.Dtos.RequestDto.TransactionRequestDto;
import com.back.banka.Dtos.ResponseDto.DeactivateAccountResponseDto;

import java.util.List;

public interface BankTransactionService {
    SendMoneyResponseDto sendMoney(Long accountId, TransactionRequestDto requestDto);
    List<DeactivateAccountResponseDto.TransactionsResponseDto> getTransactionsHistory(Long accountId);
}
