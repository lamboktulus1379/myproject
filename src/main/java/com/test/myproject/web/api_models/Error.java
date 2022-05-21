package com.test.myproject.web.api_models;

import java.util.List;

import lombok.Data;

@Data
public class Error {
    public List<Errors> errors;

    public Error(List<Errors> errors) {
        this.errors = errors;
    }
}
