package com.back.banka.Dtos.RequestDto;

import com.back.banka.Enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegisterRequestDto {



    private String fullName;
    private int age;
    private String email;
    private String password;
    private String country;
    private Rol rol;
    private boolean status;
}
