package com.back.banka.Services.IServices;

import com.back.banka.Dtos.RequestDto.RegisterRequestDto;
import com.back.banka.Dtos.ResponseDto.RegisterResponseDto;
import org.springframework.stereotype.Service;


public interface IRegisterService {
    RegisterResponseDto registerUser(RegisterRequestDto request);
}

