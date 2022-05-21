package com.test.myproject.core.order_aggregate.advice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.myproject.web.api_models.Error;
import com.test.myproject.web.api_models.Errors;

public class OrderAdvice {

    public static Map<String, Error> GenerateOrderAdvice(int statusCode, String message) {
        Errors errors = new Errors(statusCode, message);
        List<Errors> listErrors = new ArrayList<Errors>();
        listErrors.add(errors);

        Error error = new Error(listErrors);
        Map<String, Error> mapError = new HashMap<String, Error>();
        mapError.put("error", error);

        return mapError;
    }
}
