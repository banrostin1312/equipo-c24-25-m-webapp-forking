package com.back.banka.Services.Impl;

import com.back.banka.Dtos.RequestDto.ResetPasswordRequestDto;
import com.back.banka.Model.Tokens;
import com.back.banka.Model.User;
import com.back.banka.Repository.ITokenRepository;
import com.back.banka.Repository.UserRepository;
import com.back.banka.Services.IServices.IUserService;
import com.back.banka.Utils.IUtilsService;
import com.back.banka.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository   userRepository;
    private final ITokenRepository tokenRepository;
    private  final IUtilsService utilsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ITokenRepository tokenRepository,
                           IUtilsService utilsService,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.utilsService = utilsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void sentPasswordResetEmail(String email) {
        try {
            User user = this.userRepository.findByEmail(email).orElseThrow(()
                    -> new UsernameNotFoundException("Usuario no encotrado"));

            String tokenJwt = this.jwtUtil.generateResetPasswordToken(email);
            String resetUrl = "https://equipo-c24-25-m-webapp-1.onrender.com/recuperar-contraseña?t=" + tokenJwt;
            this.utilsService.saveUserToken(user, tokenJwt);
            this.utilsService.sendAccountNotification(user,
                    " Hola a través de este correo podrás configurar tu contraseña",
                    "email-template",
                    "Da click en la url para escribir tu nueva contraseña" + resetUrl
                    );
        }catch (DataAccessException e){
            throw new RuntimeException("Error al procesar la solicitud", e);
        }
    }


    @Override
    public String resetPassword(ResetPasswordRequestDto requestDto) {
        return "";
    }


}
