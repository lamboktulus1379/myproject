package com.test.myproject.core.user_aggreate.advice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.myproject.web.api_models.Errors;
import com.test.myproject.web.api_models.Error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomeHttpResponseAdvice {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleException(BindException ex) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();

        Errors err = new Errors();
        List<Errors> listErrors = new ArrayList<Errors>();

        for (FieldError fieldError : errors) {
            err.setErrorCode(400);
            err.setErrorMessage(fieldError.getDefaultMessage());
            listErrors.add(err);
        }

        Error error = new Error(listErrors);
        Map<String, Error> mapError = new HashMap<String, Error>();
        mapError.put("error", error);

        return new ResponseEntity<>(mapError, HttpStatus.BAD_REQUEST);
    }

}