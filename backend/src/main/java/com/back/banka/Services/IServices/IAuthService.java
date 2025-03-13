package com.back.banka.Services.IServices;

import com.back.banka.Dtos.RequestDto.LoginRequestDto;
import com.back.banka.Dtos.ResponseDto.GetAllUsersResponseDto;
import com.back.banka.Dtos.ResponseDto.TokenResponseDto;

import java.util.List;

public interface IAuthService {

    /**
     * metodo para autenticacion de un usuario
     * se valida a traves del username y la password
     * se devuelve token de autenticacion(JWT)
     * @return LoginResponseDto
     * */
    TokenResponseDto authenticate(LoginRequestDto loginRequestDto);
    TokenResponseDto refreshToken(String authHeader);
}
