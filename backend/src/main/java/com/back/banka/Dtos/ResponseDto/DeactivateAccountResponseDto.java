package com.back.banka.Dtos.ResponseDto;

import lombok.*;

import java.math.BigDecimal;

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

    @Getter
    @Setter
    @Builder
    public static class TransactionsResponseDto {
        private String senderAccountNumber;
        private BigDecimal amount;
        private String receiverAccountNumber;
        private String date;
    }
}
