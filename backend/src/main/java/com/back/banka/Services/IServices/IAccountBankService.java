package com.back.banka.Services.IServices;

import com.back.banka.Dtos.RequestDto.ActiveAccountRequestDto;
import com.back.banka.Dtos.ResponseDto.ActiveAccountResponseDto;

public interface IAccountBankService {

    ActiveAccountResponseDto activeAccount(ActiveAccountRequestDto requestDto);
    ActiveAccountResponseDto deactivateAccount(Integer accountNumber);
    ActiveAccountResponseDto getBalance(Long accountId);
    ActiveAccountResponseDto getBalanceByEmail(String email);
}
