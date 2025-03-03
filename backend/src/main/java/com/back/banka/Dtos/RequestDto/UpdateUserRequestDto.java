package com.back.banka.Dtos.RequestDto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequestDto {

    private String name;

    private String DNI;
    @Email(message = "Formato invalido")
    private String email;
    private LocalDate dateBirthDay;
}
