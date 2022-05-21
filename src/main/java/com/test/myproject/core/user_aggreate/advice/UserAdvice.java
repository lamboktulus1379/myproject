package com.test.myproject.core.user_aggreate.advice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.myproject.web.api_models.Errors;
import com.test.myproject.web.api_models.Error;

public class UserAdvice {

    public static Map<String, Error> GenerateUserAdvice(int statusCode, String message) {
        Errors errors = new Errors(statusCode, message);
        List<Errors> listErrors = new ArrayList<Errors>();
        listErrors.add(errors);

        Error error = new Error(listErrors);
        Map<String, Error> mapError = new HashMap<String, Error>();
        mapError.put("error", error);

        return mapError;
    }
}
