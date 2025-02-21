package com.back.banka.Exceptions.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDto {
    private String message;
    private int code;
    private LocalDate dateCreation;

    public ErrorResponseDto(String message, int code, LocalDate dateCreation) {
        this.message = message;
        this.code = code;
        this.dateCreation = dateCreation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }
}
