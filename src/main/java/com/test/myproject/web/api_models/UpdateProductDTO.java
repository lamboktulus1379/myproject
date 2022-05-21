package com.test.myproject.web.api_models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateProductDTO {
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    private String description;

    @NotBlank(message = "No is required")
    private String no;

    @NotNull(message = "Stock is required")
    private Integer stock;

    @NotNull(message = "Price is required")
    private Double price;

    @NotNull(message = "Category id is required")
    private Long categoryId;
}
