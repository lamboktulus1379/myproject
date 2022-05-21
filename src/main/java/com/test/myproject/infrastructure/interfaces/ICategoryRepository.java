package com.test.myproject.infrastructure.interfaces;

import com.test.myproject.core.category_aggregate.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    Page<Category> findByNameLike(String name, Pageable pageable);
}
