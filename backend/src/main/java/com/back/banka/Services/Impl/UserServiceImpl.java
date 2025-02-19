package com.back.banka.Services.Impl;
import com.back.banka.Model.User;
import com.back.banka.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {


    @Autowired
    private UserRepository userRepository;

    public User obtenerUsuarioPorId(Long id){
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
    }

    public User bloquearUsuario(Long id){
        User usuario = obtenerUsuarioPorId(id);
        usuario.setStatus(false);
        return userRepository.save(usuario);
    }

    public User desbloquearUsuario(Long id){
        User usuario = obtenerUsuarioPorId(id);
        usuario.setStatus(true);
        return userRepository.save(usuario);
    }

}
