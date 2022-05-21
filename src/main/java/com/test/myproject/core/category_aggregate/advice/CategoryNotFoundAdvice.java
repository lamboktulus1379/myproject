package com.test.myproject.core.category_aggregate.advice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.myproject.core.category_aggregate.exception.CategoryNotFoundException;
import com.test.myproject.web.api_models.Error;
import com.test.myproject.web.api_models.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CategoryNotFoundAdvice {
    @ExceptionHandler(value = CategoryNotFoundException.class)
    public ResponseEntity<Object> exception(CategoryNotFoundException exception) {

        Errors errors = new Errors(404, exception.getMessage());
        List<Errors> listErrors = new ArrayList<Errors>();
        listErrors.add(errors);

        Error error = new Error(listErrors);
        Map<String, Error> mapError = new HashMap<String, Error>();
        mapError.put("error", error);

        return new ResponseEntity<>(mapError, HttpStatus.NOT_FOUND);
    }
}