package com.back.banka.Services.IServices;

import com.back.banka.Dtos.RequestDto.LoginRequestDto;
import com.back.banka.Dtos.ResponseDto.LoginResponseDto;

public interface IUserService {

    /**
     * metodo para autenticacion de un usuario
     * se valida a traves del username y la password
     * se devuelve token de autenticacion(JWT)
     * @return LoginResponseDto
     * */
    LoginResponseDto authenticate(LoginRequestDto loginRequestDto);

}
