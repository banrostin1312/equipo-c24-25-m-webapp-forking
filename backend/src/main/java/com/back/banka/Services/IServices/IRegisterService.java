package com.back.banka.Services.IServices;

import com.back.banka.Dtos.RequestDto.RegisterRequestDto;
import org.springframework.stereotype.Service;


public interface IRegisterService {
    String registerUser(RegisterRequestDto request);
}

