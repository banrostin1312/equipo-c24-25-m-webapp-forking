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
    @NotNull(message = "no puede ser nulo ")
    private String document;
    @NotNull(message = "no puede ser nulo ")
    private LocalDate birthDate;
    @NotNull(message = "no puede ser nulo ")
    private String securityPhrase;
}
