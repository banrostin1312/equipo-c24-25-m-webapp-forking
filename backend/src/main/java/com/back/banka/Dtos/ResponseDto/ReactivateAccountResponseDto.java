package com.back.banka.Dtos.ResponseDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReactivateAccountResponseDto {
    private Long id;
    private  Long UserId;
    private  String numberAccount;
    private String dateOfReactivation;
    private String message;
}
