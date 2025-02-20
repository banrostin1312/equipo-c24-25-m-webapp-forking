package com.back.banka.Exceptions.Handler;


import com.back.banka.Exceptions.Custom.CustomAuthenticationException;
import com.back.banka.Exceptions.Custom.InvalidCredentialExceptions;
import com.back.banka.Exceptions.Custom.UserNotFoundException;
import com.back.banka.Exceptions.Dtos.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;

public class GlobalHandlerException {

    //lanzadores

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ErrorResponseDto> userNotFoundHandlerException(UserNotFoundException exception){
        ErrorResponseDto  errorResponseDto = ErrorResponseDto.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .dateCreation(LocalDate.now())
                .build();

        return new ResponseEntity<>(errorResponseDto,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialExceptions.class)
    private ResponseEntity<ErrorResponseDto> userNotFoundHandlerException(InvalidCredentialExceptions exception){
        ErrorResponseDto  errorResponseDto = ErrorResponseDto.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .dateCreation(LocalDate.now())
                .build();

        return new ResponseEntity<>(errorResponseDto,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    private ResponseEntity<ErrorResponseDto> userNotFoundHandlerException(CustomAuthenticationException exception){
        ErrorResponseDto  errorResponseDto = ErrorResponseDto.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .dateCreation(LocalDate.now())
                .build();

        return new ResponseEntity<>(errorResponseDto,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
