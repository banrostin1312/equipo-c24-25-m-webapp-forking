package com.back.banka.Dtos.ResponseDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class SendMoneyResponseDto {
    private String receiverAccountNumber;
    private String senderAccountNumber;
    private BigDecimal amount;
    private String message;
}
