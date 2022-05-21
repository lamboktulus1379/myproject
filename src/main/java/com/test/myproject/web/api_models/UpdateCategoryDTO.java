package com.test.myproject.web.api_models;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateCategoryDTO {
    public long id;
    @NotNull(message = "Name is required")
    public String name;
    public String description;
}
