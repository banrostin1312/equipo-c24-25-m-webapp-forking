package com.back.banka.Controllers;

import com.back.banka.Dtos.RequestDto.RegisterRequestDto;
import com.back.banka.Services.IServices.IRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IRegisterService registerService;

    @Autowired
    public AuthController(IRegisterService registerService){
        this.registerService = registerService;
    }

    //Request para registrar nuevo usuario
    @PostMapping("/registrarse")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto request){
        String response = registerService.registerUser(request);
        if (response.startsWith("Error")){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
