package com.back.banka.Dtos.RequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResetPasswordRequestDto {
    String token;
    String newPassword;
}
