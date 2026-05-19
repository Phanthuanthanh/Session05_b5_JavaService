package com.example.b5.service;

import com.example.b5.entity.Product;
import com.example.b5.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        product.setId(null);
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> updateFullProduct(Long id, Product product) {
        if (!productRepository.existsById(id)) {
            return Optional.empty();
        }
        product.setId(id);
        return Optional.of(productRepository.save(product));
    }

    @Override
    public Optional<Product> updatePartialProduct(Long id, Map<String, Object> updates) {
        return productRepository.findById(id).map(existingProduct -> {
            if (updates.containsKey("name")) {
                existingProduct.setName((String) updates.get("name"));
            }
            if (updates.containsKey("price")) {
                existingProduct.setPrice(Double.parseDouble(updates.get("price").toString()));
            }
            return productRepository.save(existingProduct);
        });
    }

    @Override
    public boolean deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }
}