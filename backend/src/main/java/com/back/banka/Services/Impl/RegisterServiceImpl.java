package com.back.banka.Services.Impl;

import com.back.banka.Dtos.RequestDto.RegisterRequestDto;
import com.back.banka.Enums.Rol;
import com.back.banka.Model.User;
import com.back.banka.Repository.UserRepository;
import com.back.banka.Services.IServices.IRegisterService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor

public class RegisterServiceImpl implements IRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailService;

    public RegisterServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    //Registra un usuario solo si el correo aún no está registrado.
    public String registerUser(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Correo ya registrado. Intenta iniciar sesión o ingresa un correo distinto.";
        } else {
            User user = new User();
            //user.setName(request.getName());   //Cambiar nombre en entidad User de "nombre" a "name"
           // user.setAge(request.getAge());     //Cambiar nombre en entidad User de "edad" a "age"
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            //user.setCountry(request.getCountry()); //Cambiar nombre en entidad de "pais" a "country"
            user.setDNI(request.getDNI());
            //user.setRole(Rol.CLIENTE);     //Cambiar nombre en entidad User y cambiar nombre de Enum "Rol" a "Role"

            userRepository.save(user);
            emailService.sendEmail(user.getEmail(), "¡Registro exitoso!/n", "Bienvenido a Luma");
            return "¡Registro Exitoso!";
        }
    }


}


