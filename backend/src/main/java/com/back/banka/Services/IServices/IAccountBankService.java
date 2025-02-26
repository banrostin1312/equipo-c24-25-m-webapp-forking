package com.back.banka.Services.IServices;

import com.back.banka.Dtos.RequestDto.ActiveAccountRequestDto;
import com.back.banka.Dtos.ResponseDto.ActiveAccountResponseDto;
import com.back.banka.Dtos.ResponseDto.DeactivateAccountResponseDto;
import com.back.banka.Dtos.ResponseDto.GetAllAccountDto;
import com.back.banka.Dtos.ResponseDto.ReactivateAccountResponseDto;

import java.util.List;

public interface IAccountBankService {

    ActiveAccountResponseDto activeAccount(ActiveAccountRequestDto requestDto);
    DeactivateAccountResponseDto deactivateAccount(Long accountId);
    List<GetAllAccountDto> getAllAccounts();
    ReactivateAccountResponseDto reactiveAccount(Long accountId);
    ActiveAccountResponseDto getBalance(Long accountId);
    ActiveAccountResponseDto getBalanceByEmail(String email);
}
