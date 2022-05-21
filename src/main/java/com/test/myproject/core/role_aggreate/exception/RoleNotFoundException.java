package com.test.myproject.core.role_aggreate.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(Long id) {
        super("Could not found role with id " + id);
    }
}