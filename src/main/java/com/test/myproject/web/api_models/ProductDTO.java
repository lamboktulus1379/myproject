package com.test.myproject.web.api_models;

import com.test.myproject.core.category_aggregate.Category;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String no;
    private Integer stock;
    private Double price;

    private Category category;
}
