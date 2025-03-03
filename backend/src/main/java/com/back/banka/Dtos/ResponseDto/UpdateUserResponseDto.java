package com.back.banka.Dtos.ResponseDto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateUserResponseDto {
    private String name;
    private String DNI;
    private String email;
    private LocalDate dateBirthDay;
}
