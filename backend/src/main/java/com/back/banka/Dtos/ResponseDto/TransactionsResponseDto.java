package com.back.banka.Dtos.ResponseDto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsResponseDto {
    private String accountNumber;
    private BigDecimal amount;
    private String date;
}
