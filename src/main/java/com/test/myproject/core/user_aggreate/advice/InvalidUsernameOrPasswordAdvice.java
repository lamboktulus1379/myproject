package com.test.myproject.core.user_aggreate.advice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.myproject.core.user_aggreate.exception.InvalidUsernameOrPasswordException;
import com.test.myproject.web.api_models.Error;
import com.test.myproject.web.api_models.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidUsernameOrPasswordAdvice {
    @ExceptionHandler(value = InvalidUsernameOrPasswordException.class)
    public ResponseEntity<Object> exception(InvalidUsernameOrPasswordException exception) {

        Errors errors = new Errors(400, exception.getMessage());
        List<Errors> listErrors = new ArrayList<Errors>();
        listErrors.add(errors);

        Error error = new Error(listErrors);
        Map<String, Error> mapError = new HashMap<String, Error>();
        mapError.put("error", error);

        return new ResponseEntity<>(mapError, HttpStatus.BAD_REQUEST);
    }
}