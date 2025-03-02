package com.back.banka.Services.Impl;
import com.back.banka.Dtos.RequestDto.ResetPasswordRequestDto;
import com.back.banka.Model.User;
import com.back.banka.Repository.ITokenRepository;
import com.back.banka.Repository.UserRepository;
import com.back.banka.Services.IServices.IUserService;
import com.back.banka.Utils.IUtilsService;
import com.back.banka.Utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.support.HttpAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository   userRepository;
    private final ITokenRepository tokenRepository;
    private  final IUtilsService utilsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ITokenRepository tokenRepository,
                           IUtilsService utilsService,
                           JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.utilsService = utilsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
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

        String username = jwtUtil.extractEmail(requestDto.getToken());

        if (requestDto.getToken() == null || !jwtUtil.validateToken(requestDto.getToken())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token inválido");
        }

        User user = this.userRepository.findByEmail(username).orElseThrow(()
                -> new UsernameNotFoundException("Usuario no encontrado"));

        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        this.userRepository.save(user);

        return "Contraseña actualizada correctamente";
    }


}
