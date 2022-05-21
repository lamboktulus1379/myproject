package com.test.myproject.core.user_aggreate.exception;

public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super("Something wrong with token, " + message);
    }
}