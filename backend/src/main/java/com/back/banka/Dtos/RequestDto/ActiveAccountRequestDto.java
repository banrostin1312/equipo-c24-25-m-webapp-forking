package com.back.banka.Dtos.RequestDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
//    @Size(min = 10, max = 100, message = "La frase de seguridad debe tener entre 10 y 100 caracteres")
    private String securityPhrase;
    private LocalDate dateOfActivation;
}
