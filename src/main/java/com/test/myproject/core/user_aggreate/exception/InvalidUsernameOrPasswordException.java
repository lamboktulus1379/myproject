package com.test.myproject.core.user_aggreate.exception;

public class InvalidUsernameOrPasswordException extends RuntimeException {
    public InvalidUsernameOrPasswordException(String email) {
        super("Invalid email " + email + " or password");
    }
}