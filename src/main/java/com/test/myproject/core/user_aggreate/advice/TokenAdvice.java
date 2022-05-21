package com.test.myproject.core.user_aggreate.advice;

import java.util.Map;

import com.test.myproject.core.user_aggreate.exception.TokenException;
import com.test.myproject.web.api_models.Error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TokenAdvice {
    @ExceptionHandler(value = TokenException.class)
    public ResponseEntity<Object> exception(TokenException exception) {
        Map<String, Error> mapError = UserAdvice.GenerateUserAdvice(403, exception.getMessage());
        return new ResponseEntity<>(mapError, HttpStatus.FORBIDDEN);
    }
}