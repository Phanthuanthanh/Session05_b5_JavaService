package com.example.b5.controller;

import com.example.b5.entity.Product;
import com.example.b5.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sortParam) {

        String[] sortArray = sortParam.split(",");
        String sortBy = sortArray[0];
        Sort.Direction direction = (sortArray.length > 1 && sortArray[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Lỗi: Dữ liệu không hợp lệ!");
        }
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateFull(@PathVariable("id") Long id, @Valid @RequestBody Product product, BindingResult result) {
        if (result.hasErrors() || product.getName() == null || product.getName().trim().isEmpty() || product.getPrice() <= 0) {
            return ResponseEntity.badRequest().body("Lỗi: Dữ liệu không đầy đủ hoặc hợp lệ!");
        }
        return productService.updateFullProduct(id, product)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<?> updatePartial(@PathVariable("id") Long id, @RequestBody Map<String, Object> updates) {
        if (updates.containsKey("name") && (updates.get("name") == null || updates.get("name").toString().trim().isEmpty())) {
            return ResponseEntity.badRequest().body("Lỗi: Tên trống!");
        }
        if (updates.containsKey("price") && Double.parseDouble(updates.get("price").toString()) <= 0) {
            return ResponseEntity.badRequest().body("Lỗi: Giá không hợp lệ!");
        }

        return productService.updatePartialProduct(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}