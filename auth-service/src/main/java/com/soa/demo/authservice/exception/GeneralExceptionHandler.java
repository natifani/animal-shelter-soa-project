package com.soa.demo.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler(RoleException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public final String handleRoleException(RoleException e) {
        return e.getMessage();
    }

    @ExceptionHandler(RefreshTokenException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public final String handleRefreshTokenException(RefreshTokenException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public final String handleUserNotFoundException(UserNotFoundException e) {
        return e.getMessage();
    }
}
