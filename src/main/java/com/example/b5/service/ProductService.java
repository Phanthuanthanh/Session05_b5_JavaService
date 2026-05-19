package com.example.b5.service;

import com.example.b5.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface ProductService {
    Page<Product> getAllProducts(Pageable pageable);
    Optional<Product> getProductById(Long id);
    Product createProduct(Product product);
    Optional<Product> updateFullProduct(Long id, Product product);
    Optional<Product> updatePartialProduct(Long id, Map<String, Object> updates);
    boolean deleteProduct(Long id);
}