package com.blubbax.esa.userManager.User.exception;

public class AuthFailedException extends RuntimeException {
    public AuthFailedException() {
        super("Authentication failed for the given credentials.");
    }
}
