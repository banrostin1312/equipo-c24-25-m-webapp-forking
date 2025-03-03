package com.back.banka.Exceptions.Custom;

public class AccountStatusException extends RuntimeException {
    public AccountStatusException(String message) {
        super(message);
    }
}
