package com.inventra.service;

import com.inventra.model.Product;
import com.inventra.repository.ProductRepository;
import com.inventra.exception.InvalidOperationException;

import org.springframework.stereotype.Service;

@Service
public class ProductValidationService {

    private final ProductRepository productRepository;

    public ProductValidationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateProduct(Product product) {

    if (product == null) {
        throw new RuntimeException("Product cannot be null");
    }

    if (product.getSku() == null || product.getSku().trim().isEmpty()) {
        throw new RuntimeException("SKU is mandatory");
    }

    if (!productRepository.findBySku(product.getSku()).isEmpty()) {
        throw new RuntimeException("Duplicate SKU not allowed");
    }

    if (product.getUnitPrice() == null || product.getUnitPrice() <= 0) {
        throw new RuntimeException("Unit price must be greater than 0");
    }

    if (product.getMinStockLevel() == null || product.getMinStockLevel() < 0) {
        throw new RuntimeException("Minimum stock level cannot be negative");
    }
}
}