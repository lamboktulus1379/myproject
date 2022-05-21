package com.test.myproject.web.api_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Errors {
    private int errorCode;
    private String errorMessage;
}
