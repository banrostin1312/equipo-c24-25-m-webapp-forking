package com.back.banka.Services.Impl;

import com.back.banka.Dtos.RequestDto.RegisterRequestDto;
import com.back.banka.Dtos.ResponseDto.RegisterResponseDto;
import com.back.banka.Enums.Rol;
import com.back.banka.Exceptions.Custom.UserAlreadyExistsException;
import com.back.banka.Model.User;
import com.back.banka.Repository.UserRepository;
import com.back.banka.Services.IServices.IRegisterService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RegisterServiceImpl implements IRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailService;

    @Autowired
    public RegisterServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    //Registra un usuario solo si el correo aún no está registrado.
    public RegisterResponseDto registerUser(RegisterRequestDto request) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new UserAlreadyExistsException("Correo ya registrado. Intenta iniciar sesión o ingresa un correo distinto.");
            }

            User user = User.builder()
                    .name(request.getName())
                    .age(request.getAge())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .country(request.getCountry())
                    .DNI(request.getDNI())
                    .status(true)
                    .role(Rol.CLIENTE)
                    .build();

            User savedUser = userRepository.save(user);
            emailService.sendEmail(user.getEmail(), "¡Registro exitoso!\n", "Bienvenido a Luma");

            return RegisterResponseDto.builder()
                    .message("¡Registro Exitoso!")
                    .userId(savedUser.getId())
                    .build();


    }
}