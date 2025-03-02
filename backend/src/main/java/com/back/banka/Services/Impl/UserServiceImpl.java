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
    private  final IUtilsService utilsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           IUtilsService utilsService,
                           JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.utilsService = utilsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     MEtodo para enviar correo para que el usaurio pueda cambiar su contraseña
     el metodo recibe el username del usuario y envia una url al usuario para que pueda resetear su contraseña
     @param email
     **/
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

/**
MEtodo para actualizar la contraseña de un usuario en la base de datos.
 Valida que el token sea enviado y lo compara con el que esta guardado en la base de datos
 si el token es valido cambia la contraseña
 @param requestDto
 @return string
**/
    @Override
    public String resetPassword(ResetPasswordRequestDto requestDto) {

       try {
           String username = jwtUtil.extractEmail(requestDto.getToken());

           if (requestDto.getToken() == null || !jwtUtil.validateToken(requestDto.getToken())) {
               throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token inválido");
           }

           User user = this.userRepository.findByEmail(username).orElseThrow(()
                   -> new UsernameNotFoundException("Usuario no encontrado"));

           user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
           this.userRepository.save(user);

           return "Contraseña actualizada correctamente";
       }catch (DataAccessException e){
            throw new RuntimeException("Error al actulizar contraseña ", e);
       }
    }


}
