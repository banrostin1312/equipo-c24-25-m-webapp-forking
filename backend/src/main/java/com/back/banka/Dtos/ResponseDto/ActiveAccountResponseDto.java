package com.back.banka.Dtos.ResponseDto;

import com.back.banka.Enums.AccountStatus;
import com.back.banka.Enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ActiveAccountResponseDto {
    private String accountNumber;
    private String accountType;
    private String accountStatus;
    private BigDecimal balance;

}
