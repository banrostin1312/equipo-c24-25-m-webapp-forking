package com.back.banka.Services.Impl;

import com.back.banka.Dtos.RequestDto.RegisterRequestDto;
import com.back.banka.Enums.Rol;
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
    public String registerUser(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException ("Correo ya registrado. Intenta iniciar sesión o ingresa un correo distinto.");
        }
            User user = new User();
            user.setName(request.getName());
            user.setAge(request.getAge());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setCountry(request.getCountry());
            user.setDNI(request.getDNI());
            user.setRole(Rol.CLIENTE);

            User savedUser = userRepository.save(user);
            emailService.sendEmail(user.getEmail(), "¡Registro exitoso!/n", "Bienvenido a Luma");
            return new RegisterRequestDto("¡Registro Exitoso!", savedUser.getId());
        }
    }


}


