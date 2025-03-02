package com.back.banka.Services.Impl;
import com.back.banka.Dtos.RequestDto.ResetPasswordRequestDto;
import com.back.banka.Dtos.ResponseDto.GetAllUsersResponseDto;
import com.back.banka.Model.User;
import com.back.banka.Repository.UserRepository;
import com.back.banka.Services.IServices.IEmailService;
import com.back.banka.Services.IServices.IUserService;
import com.back.banka.Utils.IUtilsService;
import com.back.banka.Utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository   userRepository;
    private  final IUtilsService utilsService;
    private final IEmailService emailService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           IUtilsService utilsService, IEmailService emailService,
                           JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.utilsService = utilsService;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     MEtodo para enviar correo para que el usaurio pueda cambiar su contraseña
     el metodo recibe el username del usuario y envia una url al usuario para que pueda resetear su contraseña
     @param email
     **/
    @Override
    public void sendPasswordResetEmail(String email) {
        try {
            User user = this.userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            String tokenJwt = this.jwtUtil.generateResetPasswordToken(email);
            String resetUrl = "https://equipo-c24-25-m-webapp-1.onrender.com/recuperar-contrasenia?token=" + tokenJwt;

            this.utilsService.saveUserToken(user, tokenJwt);

            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("name", user.getName());
            emailVariables.put("message", "Hola, a través de este correo podrás configurar tu contraseña.");
            emailVariables.put("resetUrl", resetUrl);
            try {
                this.emailService.sendEmailTemplate(
                        user.getEmail(),
                        "Hola, a través de este correo podrás configurar tu contraseña.",
                        "reset-password",
                        emailVariables
                );
            } catch (Exception e) {
                log.error("Error al enviar el correo de restablecimiento de contraseña a {}: {}", email, e.getMessage(), e);
            }

        } catch (DataAccessException e) {
            log.error("Error de base de datos al procesar la solicitud de restablecimiento de contraseña: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar la solicitud. Intente más tarde.");
        }
    }

    /**
MEtodo para actualizar la contraseña de un usuario en la base de datos.
 Valida que el token sea enviado y lo compara con el que esta guardado en la base de datos
 si el token es valido cambia la contraseña
 @param requestDto
 @return string
**/
    @Transactional
    @Override
    public String resetPassword(ResetPasswordRequestDto requestDto) {
        try {
            log.info("Iniciando resetPassword para el token: " + requestDto.getToken());

            String username = jwtUtil.extractEmail(requestDto.getToken());
            if (username == null || username.isEmpty()) {
                log.error("El email extraído es nulo o vacío");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
            }
            log.info("Email extraído: " + username);

            if (requestDto.getToken() == null || !jwtUtil.validateToken(requestDto.getToken())) {
                log.error("Token inválido");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
            }

            User user = userRepository.findByEmail(username).orElseThrow(() -> {
                log.error("Usuario no encontrado para el email: " + username);
                return new UsernameNotFoundException("Usuario no encontrado");
            });
            log.info("Usuario encontrado: " + user.getEmail());

            user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
            this.userRepository.save(user);
            log.info("Contraseña actualizada correctamente");

            try {
                this.utilsService.sendAccountNotification(
                        user,
                        "Cambio de contraseña",
                        "reset-password",
                        "Contraseña actualizada"
                );
            } catch (Exception e) {
                log.error("Error al enviar correo", e);
            }

            return "Contraseña actualizada correctamente";
        } catch (Exception e) {
            log.error("Error en resetPassword", e);
            throw e;
        }
    }


    @Transactional(readOnly = true)
    @Override
    public List<GetAllUsersResponseDto> getAllUsers() {
        try {
            List<User> allUsers = this.userRepository.findAll();

            return allUsers.stream()
                    .map(user -> GetAllUsersResponseDto.builder()
                            .name(user.getName())
                            .DNI(user.getDNI())
                            .birthday(String.valueOf(user.getDateBirthDay()))
                            .email(user.getEmail())
                            .country(user.getCountry())
                            .status(user.isStatus())
                            .build())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error al traer usuarios", e);
        }
    }
}
