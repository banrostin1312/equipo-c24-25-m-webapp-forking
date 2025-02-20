package com.back.banka.Services.Impl;
import com.back.banka.Dtos.RequestDto.LoginRequestDto;
import com.back.banka.Dtos.ResponseDto.LoginResponseDto;
import com.back.banka.Repository.UserRepository;

import com.back.banka.Services.IServices.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {


    @Autowired
    private UserRepository userRepository;


    @Override
    public LoginResponseDto authenticate(LoginRequestDto loginRequestDto) {
        return null;
    }
}
