package com.test.myproject.core.role_aggreate.advice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.myproject.core.role_aggreate.exception.RoleAlreadyExistsException;
import com.test.myproject.web.api_models.Error;
import com.test.myproject.web.api_models.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RoleAlreadyExistsAdvice {
    @ExceptionHandler(value = RoleAlreadyExistsException.class)
    public ResponseEntity<Object> exception(RoleAlreadyExistsException exception) {
        Map<String, Error> mapError = RoleAdvice.GenerateRoleAdvice(400, exception.getMessage());
        return new ResponseEntity<>(mapError, HttpStatus.BAD_REQUEST);
    }
}