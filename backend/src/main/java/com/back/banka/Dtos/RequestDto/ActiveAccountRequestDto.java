package com.back.banka.Dtos.RequestDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ActiveAccountRequestDto {
    @NotNull(message = "Documento no puede ser nulo ")
    private String document;
    @NotNull(message = "Su fecha de nacimiento no puede estar vacia ")
    private LocalDate birthDate;
    @NotNull(message = " La frase de seguridad  es obligatoria")
    private String securityPhrase;
}
