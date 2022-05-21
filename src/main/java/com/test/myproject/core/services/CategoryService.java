package com.test.myproject.core.services;

import com.test.myproject.core.category_aggregate.Category;
import com.test.myproject.core.category_aggregate.exception.CategoryAlreadyExistsException;
import com.test.myproject.core.category_aggregate.exception.CategoryNotFoundException;
import com.test.myproject.core.interfaces.ICategoryService;
import com.test.myproject.infrastructure.interfaces.ICategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        Category categoryExists = this.getCategoryByName(category.name);
        if (categoryExists != null) {
            throw new CategoryAlreadyExistsException(category.name);
        }
        return categoryRepository.save(category);
    }

    @Override
    public Page<Category> all(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category one(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public Category findAndUpdate(Category newCategory, Long id) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(newCategory.getName());
            category.setDescription(newCategory.getDescription());
            return categoryRepository.save(category);
        }).orElseGet(() -> {
            newCategory.setId(id);
            return categoryRepository.save(newCategory);
        });
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Page<Category> allByNameLike(String name, Pageable pageable) {
        return categoryRepository.findByNameLike("%" + name + "%", pageable);
    }
}
