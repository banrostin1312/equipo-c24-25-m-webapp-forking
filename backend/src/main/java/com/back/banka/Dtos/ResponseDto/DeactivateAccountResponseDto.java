package com.back.banka.Dtos.ResponseDto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeactivateAccountResponseDto {
    private String numberAccount;
    private Long UserId;
    private String statusAccount;
    private String dateDeactivated;
}
