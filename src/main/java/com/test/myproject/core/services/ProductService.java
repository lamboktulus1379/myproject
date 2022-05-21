package com.test.myproject.core.services;

import com.test.myproject.core.category_aggregate.Category;
import com.test.myproject.core.category_aggregate.exception.CategoryNotFoundException;
import com.test.myproject.core.interfaces.IProductService;
import com.test.myproject.core.product_aggreate.Product;
import com.test.myproject.core.product_aggreate.exception.ProductAlreadyExistsException;
import com.test.myproject.core.product_aggreate.exception.ProductNoAlreadyExistsException;
import com.test.myproject.core.product_aggreate.exception.ProductNotFoundException;
import com.test.myproject.infrastructure.interfaces.ICategoryRepository;
import com.test.myproject.infrastructure.interfaces.IProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Product save(Product product) {
        Product productNoExists = this.getProductByNo(product.getNo());
        if (productNoExists != null) {
            throw new ProductNoAlreadyExistsException(product.getNo());
        }
        Product productExists = this.getProductByName(product.getName());
        if (productExists != null) {
            throw new ProductAlreadyExistsException(product.getName());
        }
        return productRepository.save(product);
    }

    @Override
    public Page<Product> all(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product one(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product findAndUpdate(Product newProduct, Long id) {
        boolean categoryExists = categoryRepository.existsById(newProduct.getCategory().id);
        if (categoryExists) {
            return productRepository.findById(id).map(product -> {
                product.setName(newProduct.getName());
                product.setStock(newProduct.getStock());
                product.setNo(newProduct.getNo());
                return productRepository.save(product);
            }).orElseGet(() -> {
                newProduct.setId(id);
                return productRepository.save(newProduct);
            });
        }
        throw new CategoryNotFoundException(newProduct.getCategory().id);
    }

    @Override
    public void deleteById(Long id) {
        boolean productExists = productRepository.existsById(id);
        if (!productExists) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public Product getProductByNo(String no) {
        return productRepository.findByNo(no);
    }

    @Override
    public Page<Product> allByNameLike(String name, Pageable pageable) {
        return productRepository.findByNameLike("%" + name + "%", pageable);
    }
}
