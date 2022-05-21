package com.test.myproject.web.api_models;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class CreateRoleDTO {
    public long id;
    @NotEmpty(message = "Name is required")
    public String name;
}
