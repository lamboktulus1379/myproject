package com.test.myproject.core.interfaces;

import com.test.myproject.core.category_aggregate.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    public Page<Category> all(Pageable pageable);

    public Category save(Category category);

    public Category one(Long id);

    public Category findAndUpdate(Category category, Long id);

    public void deleteById(Long id);

    public Category getCategoryByName(String name);

    public Page<Category> allByNameLike(String name, Pageable pageable);
}
