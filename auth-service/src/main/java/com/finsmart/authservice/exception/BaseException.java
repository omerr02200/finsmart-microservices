package com.finsmart.authservice.exception;

public class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }

    public BaseException() {
    }
}
