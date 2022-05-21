package com.test.myproject.core.product_aggreate.advice;

import java.util.Map;

import com.test.myproject.core.product_aggreate.exception.ProductNoAlreadyExistsException;
import com.test.myproject.web.api_models.Error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductNoAlreadyExistsAdvice {
    @ExceptionHandler(value = ProductNoAlreadyExistsException.class)
    public ResponseEntity<Object> exception(ProductNoAlreadyExistsException exception) {
        Map<String, Error> mapError = ProductAdvice.GenerateProductAdvice(400, exception.getMessage());
        return new ResponseEntity<>(mapError, HttpStatus.BAD_REQUEST);
    }
}
