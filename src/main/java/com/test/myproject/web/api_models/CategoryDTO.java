package com.test.myproject.web.api_models;

import com.test.myproject.core.category_aggregate.Category;

import lombok.Data;

@Data
public class CategoryDTO {
    public long id;
    public String name;
    public String description;
}
