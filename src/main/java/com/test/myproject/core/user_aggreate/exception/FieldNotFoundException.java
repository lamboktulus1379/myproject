package com.test.myproject.core.user_aggreate.exception;

public class FieldNotFoundException extends RuntimeException {
    public FieldNotFoundException(String field) {
        super("Could not field " + field);
    }
}