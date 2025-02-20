package com.back.banka.Services.Impl;
import com.back.banka.Dtos.RequestDto.LoginRequestDto;
import com.back.banka.Dtos.ResponseDto.LoginResponseDto;
import com.back.banka.Exceptions.Custom.CustomAuthenticationException;
import com.back.banka.Exceptions.Custom.InvalidCredentialExceptions;
import com.back.banka.Repository.UserRepository;
import com.back.banka.Services.IServices.IUserService;
import com.back.banka.Utils.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class UserServiceImpl implements IUserService {



    private  final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, AuthenticationManagerBuilder authenticationManagerBuilder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtService = jwtService;
    }


    /**
     * Método para autenticar un cliente.
     * Este método realiza los siguientes pasos:
     *   Se crea el objeto de autenticación a partir de las credenciales proporcionadas (email y password)
     *   Se autentica el usuario usando el AuthenticationManager
     *   Se guarda la autenticación en el contexto de seguridad
     *   Si todo es exitoso, se genera un token JWT que será utilizado para futuras peticiones
     * @param loginRequestDto
     * @return LoginResponseDto
     *
     * @throws InvalidCredentialExceptions Si las credenciales del usuario son incorrectas o el proceso de autenticación falla.
     * @throws CustomAuthenticationException Si ocurre un error inesperado durante la autenticación.
     */



        @Override
    public LoginResponseDto authenticate(LoginRequestDto loginRequestDto) {

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword());
            try{
                Authentication authentication =
                        authenticationManagerBuilder.getObject().authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = this.jwtService.generateToken(authentication);
                return LoginResponseDto.builder()
                        .token(token)
                        .build();
            } catch (InvalidCredentialExceptions exceptions){
                throw new InvalidCredentialExceptions("Usuario o contraseña incorrectos");
            }catch (CustomAuthenticationException e){
                throw  new CustomAuthenticationException("Error de autenticacion");
            }

    }
}
