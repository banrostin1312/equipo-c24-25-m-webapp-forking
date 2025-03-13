package com.back.banka.Controllers;


import com.back.banka.Dtos.RequestDto.LoginRequestDto;
import com.back.banka.Dtos.RequestDto.RegisterRequestDto;
import com.back.banka.Dtos.ResponseDto.TokenResponseDto;
import com.back.banka.Dtos.ResponseDto.RegisterResponseDto;
import com.back.banka.Services.IServices.IRegisterService;
import com.back.banka.Services.IServices.IAuthService;
import com.back.banka.Utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/banca/auth")
public class AuthController {

    private final IAuthService authService;
    private final IRegisterService registerService;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);


    @Autowired
     public AuthController (IAuthService authService, IRegisterService registerService) {
        this.authService = authService;
         this.registerService = registerService;
    }

    @Operation(summary = "Autenticar usuario", description = "Autentica un usuario con email y contraseña")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> authenticateUser (@Valid  @RequestBody LoginRequestDto requestDto){
        TokenResponseDto loginResponseDto = this.authService.authenticate(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
    }



    //Request para registrar nuevo usuario
    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario en la aplicación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta (datos inválidos)"),
            @ApiResponse(responseCode = "409", description = "El usuario ya está registrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/registrarse")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto request){
        RegisterResponseDto response = registerService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Refrescar token" +
            "", description = "este endpoint permite que el token sea actualizado para acceder a los servicios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "token refresh exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta (datos inválidos)"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/refresh-token")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TokenResponseDto> refreshAccessToken(
            @RequestHeader(name = "Authorization", required = false) String authHeader) {
        logger.info("Authorization Header recibido: " + authHeader);

        TokenResponseDto tokenResponse = authService.refreshToken(authHeader);
        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(
            summary = "Cerrar sesión",
            description = "Cierra la sesión invalidando el token actual",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sesión cerrada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return ResponseEntity.ok("Sesión cerrada correctamente");
    }


}
