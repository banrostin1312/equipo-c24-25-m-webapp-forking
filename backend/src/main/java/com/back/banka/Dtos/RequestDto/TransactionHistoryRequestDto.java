package com.back.banka.Dtos.RequestDto;

import com.back.banka.Enums.TransactionType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data

public class TransactionHistoryRequestDto {
    private String accountNumber;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TransactionType transactionType;
}


