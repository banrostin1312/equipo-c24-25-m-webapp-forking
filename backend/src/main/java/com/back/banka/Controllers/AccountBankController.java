package com.back.banka.Controllers;

import com.back.banka.Dtos.RequestDto.ActiveAccountRequestDto;
import com.back.banka.Dtos.ResponseDto.ActiveAccountResponseDto;
import com.back.banka.Services.IServices.IAccountBankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/banca/cuenta-bancaria")
public class AccountBankController {

    private final IAccountBankService accountBankService;

    @Autowired
    public AccountBankController(IAccountBankService accountBankService) {
        this.accountBankService = accountBankService;
    }
    @Operation(summary = "Activar cuenta bancaria", description = "Permite activar una cuenta bancaria para un usuario registrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta activada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes (frase de seguridad, documento o fecha de nacimiento)"),
            @ApiResponse(responseCode = "401", description = "No autorizado (token inválido o sesión expirada)"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (usuario sin permisos para activar la cuenta)"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @PostMapping("/activar")
    public ResponseEntity<ActiveAccountResponseDto> activeAccount( @Valid  @RequestBody ActiveAccountRequestDto requestDto){
        ActiveAccountResponseDto activeAccount = this.accountBankService.activeAccount(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(activeAccount);
    }
}
