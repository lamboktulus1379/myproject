package com.test.myproject.core.product_aggreate.advice;

import java.util.Map;

import com.test.myproject.core.product_aggreate.exception.ProductOutOfStockException;
import com.test.myproject.web.api_models.Error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ProductOutOfStockAdvice {
    @ResponseBody
    @ExceptionHandler(ProductOutOfStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> ProductOutOfStockExceptionHandler(ProductOutOfStockException exception) {
        Map<String, Error> mapError = ProductAdvice.GenerateProductAdvice(400, exception.getMessage());
        return new ResponseEntity<>(mapError, HttpStatus.BAD_REQUEST);
    }
}