package com.test.myproject.core.role_aggreate.exception;

public class RoleAlreadyExistsException extends RuntimeException {
    public RoleAlreadyExistsException(String role) {
        super("Role " + role + " already exists");
    }
}