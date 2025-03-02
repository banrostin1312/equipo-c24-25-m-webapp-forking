package com.back.banka.Controllers;

import com.back.banka.Dtos.RequestDto.ResetPasswordRequestDto;
import com.back.banka.Dtos.ResponseDto.GetAllUsersResponseDto;
import com.back.banka.Services.IServices.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/banca/users")
public class UserController {
     private final IUserService userService;

     @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Restablecer contraseña",
            description = "Envia correo de restablecimiento de contraseña este correo contiene la url donde se puede restablecer la contraseña" +
                    "cuando un usuario da click alli debe ser redirigido a un formulario en el front donde pueda poner su nueva contraseña" +
                    "el front debe capturar la url y extraer el token que debe enviar por body junto a la nueva contraseña"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "contraseña enviada correctamente")
    })
    @PostMapping("/enviar-correo-reestablecer/{username}")
    public ResponseEntity<?> sendResetPassword(
            @Valid
            @RequestParam String username){
        this.userService.sendPasswordResetEmail(username);
        return ResponseEntity.status(HttpStatus.OK).body("Correo de restablecimiento enviado correctamente");
    }
    @Operation(
            summary = "Metodo para actualiazar la contraseña",
            description = "Luego de que el correo de restablecimiento es enviado, " +
                    "el usuario puede cambiar su contraseña a través de este endpoint"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "contraseña cambiada correctamente"),
            @ApiResponse(responseCode = "400", description = "request erroneo")
    })
    @PostMapping("/recuperar-contrasenia")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDto requestDto){
        log.info("Recibiendo solicitud de recuperación de contraseña: " + requestDto);
        String response = this.userService.resetPassword(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "Obtener todos los usuarios de la base de datos" +
            "", description = "este endpoint permite obtener todos los usuarios registrados en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "datos obtenidos con exito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuarios")
    public ResponseEntity<List<GetAllUsersResponseDto>> getAllUsers(){
        List<GetAllUsersResponseDto> getAllUser = this.userService.getAllUsers();
        log.info("Datos recuperadoso: ");
        return ResponseEntity.status(HttpStatus.OK).body(getAllUser);

    }

}
