package com.back.banka.Services.IServices;

import com.back.banka.Dtos.RequestDto.ResetPasswordRequestDto;
import com.back.banka.Model.User;

public interface IUserService {
    void sentPasswordResetEmail(String email);
    String resetPassword(ResetPasswordRequestDto requestDto);
}
