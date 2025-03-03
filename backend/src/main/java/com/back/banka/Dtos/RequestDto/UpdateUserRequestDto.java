package com.back.banka.Dtos.RequestDto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequestDto {
    @NotBlank(message = "El nombre no puede ser nulo")
    private String name;
    @NotBlank(message = "El DNI no puede ser nulo")
    private String DNI;
    @NotBlank(message = "El email no puede ir vacío")
    @Email
    private String email;
    @NotNull
    private LocalDate dateBirthDay;
}
