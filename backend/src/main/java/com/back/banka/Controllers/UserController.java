package com.back.banka.Controllers;

import com.back.banka.Dtos.RequestDto.ResetPasswordRequestDto;
import com.back.banka.Services.IServices.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            description = "Envia correo de restablecimiento de contraseña"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "contraseña enviada correctamente")
    })
    public ResponseEntity<String> sentResetPassword(@RequestParam String username){
        this.userService.sentPasswordResetEmail(username);
        return ResponseEntity.status(HttpStatus.OK).body("Correo de restablecimiento enviado correctamente");
    }



    @Operation(
            summary = "Metodo para actualiazar la contraseña",
            description = "Luego de que el correo de restablecimiento es enviado, el usuario puede cambiar su contraseña a través de este endpoint"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "contraseña cambiada correctamente"),
            @ApiResponse(responseCode = "400", description = "request erroneo")
    })
    @PostMapping("/recuperar-contraseña")
    public ResponseEntity<String> resetPassword(@Valid  @RequestBody ResetPasswordRequestDto requestDto){
         return ResponseEntity.status(HttpStatus.OK).body("Contraseña restablecida correctamente");
    }
}
