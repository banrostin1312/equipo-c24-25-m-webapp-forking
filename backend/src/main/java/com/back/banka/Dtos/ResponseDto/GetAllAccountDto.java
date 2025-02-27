package com.back.banka.Dtos.ResponseDto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllAccountDto {
    private Long id;
    private String statusAccount;
    private String numberAccount;
    private Long userId;
    private BigDecimal balanceAccount;
    private String dateOfCreation;
}
