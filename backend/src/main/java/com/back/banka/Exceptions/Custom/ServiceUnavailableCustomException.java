package com.back.banka.Exceptions.Custom;

public class ServiceUnavailableCustomException extends RuntimeException {
    public ServiceUnavailableCustomException(String message) {
        super(message);
    }
}
