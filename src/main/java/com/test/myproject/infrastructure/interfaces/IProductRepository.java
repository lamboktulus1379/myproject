package com.test.myproject.infrastructure.interfaces;

import com.test.myproject.core.product_aggreate.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    Product findByNo(String no);

    Page<Product> findByNameLike(String name, Pageable pageable);
}
