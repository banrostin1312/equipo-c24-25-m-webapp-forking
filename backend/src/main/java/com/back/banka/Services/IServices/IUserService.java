package com.back.banka.Services.IServices;

import com.back.banka.Dtos.RequestDto.ResetPasswordRequestDto;
import com.back.banka.Dtos.ResponseDto.GeneralResponseDto;
import com.back.banka.Dtos.ResponseDto.GetAllUsersResponseDto;
import com.back.banka.Model.User;

import java.util.List;

public interface IUserService {
    void sendPasswordResetEmail(String email);
    GeneralResponseDto resetPassword(ResetPasswordRequestDto requestDto);
    List<GetAllUsersResponseDto> getAllUsers();
}


