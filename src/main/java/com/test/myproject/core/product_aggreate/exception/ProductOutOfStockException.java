package com.test.myproject.core.product_aggreate.exception;

public class ProductOutOfStockException extends RuntimeException {
    public ProductOutOfStockException(Long id) {
        super("Some product is out of stock, please remove it " + id);
    }
}