package com.back.banka.Dtos.ResponseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor

@Builder
public class TokenResponseDto {
    @JsonProperty(value = "access_token")
    private  String token;
    @JsonProperty(value = "refresh_token")

    private String refreshToken;


}
