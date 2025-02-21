package com.back.banka.Exceptions.Handler;


import com.back.banka.Exceptions.Custom.CustomAuthenticationException;
import com.back.banka.Exceptions.Custom.InvalidCredentialExceptions;
import com.back.banka.Exceptions.Custom.UserAlreadyExistsException;
import com.back.banka.Exceptions.Custom.UserNotFoundException;
import com.back.banka.Exceptions.Dtos.ErrorResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;

@Builder
@Getter
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
    public ResponseEntity<ErrorResponseDto> handleInvalidCredentials(InvalidCredentialExceptions exception){
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(exception.getMessage())
                .dateCreation(LocalDate.now())
                .build();

        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(CustomAuthenticationException exception){
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .dateCreation(LocalDate.now())
                .build();

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .dateCreation(LocalDate.now())
                .build();

        return new ResponseEntity<>(errorResponseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException ex) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Error interno del servidor: " + ex.getMessage())
                .dateCreation(LocalDate.now())
                .build();

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handlerIlegalArgumentException(IllegalArgumentException ex) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .dateCreation(LocalDate.now())
                .build();

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .dateCreation(LocalDate.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}


