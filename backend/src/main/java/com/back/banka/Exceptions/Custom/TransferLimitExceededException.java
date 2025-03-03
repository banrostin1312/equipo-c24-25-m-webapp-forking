package com.back.banka.Exceptions.Custom;

public class TransferLimitExceededException extends RuntimeException {
    public TransferLimitExceededException(String message) {
        super(message);
    }
}
