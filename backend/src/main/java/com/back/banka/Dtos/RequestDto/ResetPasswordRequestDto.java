package com.back.banka.Dtos.RequestDto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResetPasswordRequestDto {
    @NotNull(message = "El token es obligatorio no puede estar vacio")
    String token;
    @NotNull(message = "la contraseña no puede estar vacia")
    String newPassword;
}
