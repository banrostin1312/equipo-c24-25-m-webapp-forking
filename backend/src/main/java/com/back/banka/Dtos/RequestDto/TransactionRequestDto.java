package com.back.banka.Dtos.RequestDto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class TransactionRequestDto {
    private String receiverAccountNumber;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
}



