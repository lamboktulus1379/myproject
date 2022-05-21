package com.test.myproject.core.product_aggreate.exception;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String name) {
        super("Product with name " + name + " already exists");
    }
}