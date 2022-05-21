package com.blubbax.esa.userManager.User.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DuplicateUserAdvice {

    @ResponseBody
    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String duplicateUserHandler(DuplicateUserException ex) {
        return ex.getMessage();
    }
}
