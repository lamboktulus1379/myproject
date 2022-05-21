package com.test.myproject.web.api_models;

import lombok.Data;

@Data
public class ProductOrderItemDTO {
    private Long id;
    private String name;
    private String no;
    private Integer stock;
    private Double price;
    private CategoryDTO Category;
}
