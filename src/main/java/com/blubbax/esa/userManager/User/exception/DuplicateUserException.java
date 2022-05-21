package com.blubbax.esa.userManager.User.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String username) {
        super("Username " + username + " already in use");
    }
}
