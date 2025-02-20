package com.back.banka.Controllers;


import com.back.banka.Dtos.RequestDto.LoginRequestDto;
import com.back.banka.Dtos.ResponseDto.LoginResponseDto;
import com.back.banka.Dtos.ResponseDto.RegisterResponseDto;
import com.back.banka.Services.IServices.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/banca/auth")
public class AuthController {

    private final IUserService userService;

    @Autowired
    public AuthController(IUserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticateUser (@Valid  @RequestBody LoginRequestDto requestDto){
        LoginResponseDto loginResponseDto = this.userService.authenticate(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
    }

}
