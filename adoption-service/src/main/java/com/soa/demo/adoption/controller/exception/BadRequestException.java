package com.soa.demo.adoption.controller.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super();
    }
    public BadRequestException(String error) {
        super(error);
    }
}
