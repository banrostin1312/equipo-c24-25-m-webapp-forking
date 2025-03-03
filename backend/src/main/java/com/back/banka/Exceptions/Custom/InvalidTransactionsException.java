package com.back.banka.Exceptions.Custom;

public class InvalidTransactionsException extends RuntimeException {
    public InvalidTransactionsException(String message) {
        super(message);
    }
}
