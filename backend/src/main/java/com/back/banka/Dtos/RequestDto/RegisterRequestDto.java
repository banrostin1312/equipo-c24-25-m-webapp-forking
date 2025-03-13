package com.back.banka.Dtos.RequestDto;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequestDto {
    @NotBlank(message = "Campo obligatorio")
    private String name;
    @NotNull(message = "Campo obligatorio")

    private int age;
    @NotBlank(message = "Campo obligatorio")
    @Email(message = "Formato inválido")
    private String email;
    @NotBlank(message = "Campo obligatorio")
    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial"
    )
    private String password;
    @NotBlank(message = "Campo obligatorio")
    private String country;
    @NotBlank(message = "Campo obligatorio")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe contener solo números " +
            "y tener exactamente 8 dígitos")
    @Column(name = "dni", nullable = false, unique = true)
    private String DNI;

}
