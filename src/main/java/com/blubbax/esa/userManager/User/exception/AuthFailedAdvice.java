package com.blubbax.esa.userManager.User.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AuthFailedAdvice {

    @ResponseBody
    @ExceptionHandler(AuthFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String authFailedHandler(AuthFailedException ex) {
        return ex.getMessage();
    }
}
