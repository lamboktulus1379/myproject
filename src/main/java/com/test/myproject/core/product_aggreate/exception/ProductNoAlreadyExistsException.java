package com.test.myproject.core.product_aggreate.exception;

public class ProductNoAlreadyExistsException extends RuntimeException {
    public ProductNoAlreadyExistsException(String no) {
        super("Product with no " + no + " already exists");
    }
}