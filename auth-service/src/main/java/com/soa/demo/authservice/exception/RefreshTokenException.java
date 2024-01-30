package com.soa.demo.authservice.exception;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String msg) {
        super(msg);
    }
}
