package com.test.myproject.web.api_models;

import lombok.Data;

@Data
public class CreateUserRoleDTO {
    private String email;
    private String role;
}
