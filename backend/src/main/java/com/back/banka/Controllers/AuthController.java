package com.back.banka.Controllers;

import com.back.banka.Model.User;
import com.back.banka.Utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthController {
    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        if("Ingresa tu email".equals(user.getEmail())&& "password".equals(user.getPassword())){
            return jwtUtil.generateToken(user.getEmail());
        }
        return "Invalid credentials";
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam String token){
        return jwtUtil.validateToken(token)?"Valid Token":"Invalid Token";
    }

    @GetMapping("/extract")
    public String extractEmail(@RequestParam String token){
        return jwtUtil.extractEmail(token);
    }
}
