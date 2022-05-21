package com.test.myproject.core.category_aggregate.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Could not found category with id " + id);
    }
}