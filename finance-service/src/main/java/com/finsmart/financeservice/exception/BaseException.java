package com.finsmart.financeservice.exception;

public class BaseException extends RuntimeException{
    public BaseException(String message){
        super(message);
    }

    public BaseException() {
    }
}
