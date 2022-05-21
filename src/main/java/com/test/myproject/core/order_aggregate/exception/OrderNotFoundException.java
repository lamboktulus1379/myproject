package com.test.myproject.core.order_aggregate.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Could not found order with id " + id);
    }
}