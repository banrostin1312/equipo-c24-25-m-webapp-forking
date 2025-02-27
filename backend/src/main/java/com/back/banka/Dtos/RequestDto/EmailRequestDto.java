package com.back.banka.Dtos.RequestDto;

import lombok.Data;

import java.util.Map;

@Data
public class EmailRequestDto {
    private String to;
    private String subject;
    private String content;
}
