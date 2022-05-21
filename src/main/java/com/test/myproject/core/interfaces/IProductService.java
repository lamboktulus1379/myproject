package com.test.myproject.core.interfaces;

import com.test.myproject.core.product_aggreate.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    public Page<Product> all(Pageable pageable);

    public Product save(Product product);

    public Product one(Long id);

    public Product findAndUpdate(Product product, Long id);

    public void deleteById(Long id);

    public Product getProductByName(String name);

    public Product getProductByNo(String no);

    public Page<Product> allByNameLike(String name, Pageable pageable);
}
