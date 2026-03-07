package com.inventra.service;

import com.inventra.dto.DashboardAnalytics;
import com.inventra.exception.InvalidOperationException;
import com.inventra.exception.ResourceNotFoundException;
import com.inventra.model.*;
import com.inventra.repository.ProductRepository;
import com.inventra.repository.InventoryTransactionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryTransactionRepository transactionRepository;

    // ===========================
    // ADD PRODUCT
    // ===========================

    public Product addProduct(Product product) {

        if (product.getSku() == null || product.getSku().trim().isEmpty())
            throw new InvalidOperationException("SKU is mandatory");

        if (productRepository.findBySku(product.getSku()).isPresent())
            throw new InvalidOperationException("Duplicate SKU not allowed");

        if (product.getUnitPrice() == null || product.getUnitPrice() <= 0)
            throw new InvalidOperationException("Invalid unit price");

        if (product.getMinStockLevel() == null || product.getMinStockLevel() < 0)
            throw new InvalidOperationException("Invalid minimum stock level");

        if (product.getStatus() == null)
            product.setStatus(ProductStatus.ACTIVE);

        if (product.getStockQuantity() == null)
            product.setStockQuantity(0);

        return productRepository.save(product);
    }

    // ===========================
    // GET ALL PRODUCTS
    // ===========================

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ===========================
    // GET PRODUCT BY ID
    // ===========================

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));
    }

    // ===========================
    // DELETE PRODUCT
    // ===========================

    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id))
            throw new ResourceNotFoundException("Product not found");

        productRepository.deleteById(id);
    }

    // ===========================
    // STOCK IN
    // ===========================

    @Transactional
    public String stockIn(Long productId,
                          Integer quantity,
                          String username,
                          String note) {

        if (quantity == null || quantity <= 0)
            throw new InvalidOperationException("Quantity must be greater than zero");

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        if (product.getStatus() != ProductStatus.ACTIVE)
            throw new InvalidOperationException("Cannot modify inactive product");

        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProduct(product);
        transaction.setType(TransactionType.STOCK_IN);
        transaction.setQuantity(quantity);
        transaction.setBalanceAfterTransaction(product.getStockQuantity());
        transaction.setPerformedBy(username);
        transaction.setReferenceNote(note);

        transactionRepository.save(transaction);

        return "Stock added successfully";
    }

    // ===========================
    // STOCK OUT
    // ===========================

    @Transactional
    public String stockOut(Long productId,
                           Integer quantity,
                           String username,
                           String note) {

        if (quantity == null || quantity <= 0)
            throw new InvalidOperationException("Quantity must be greater than zero");

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        if (product.getStatus() != ProductStatus.ACTIVE)
            throw new InvalidOperationException("Cannot modify inactive product");

        if (product.getStockQuantity() < quantity)
            throw new InvalidOperationException("Insufficient stock");

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProduct(product);
        transaction.setType(TransactionType.STOCK_OUT);
        transaction.setQuantity(quantity);
        transaction.setBalanceAfterTransaction(product.getStockQuantity());
        transaction.setPerformedBy(username);
        transaction.setReferenceNote(note);

        transactionRepository.save(transaction);

        if (product.getStockQuantity() < product.getMinStockLevel()) {
            return "Stock removed. WARNING: Product below minimum stock level!";
        }

        return "Stock removed successfully";
    }

    // ===========================
    // LOW STOCK PRODUCTS
    // ===========================

    public List<Product> getLowStockProducts() {
        return productRepository.findByStockQuantityLessThan(5);
    }

    // ===========================
    // DASHBOARD ANALYTICS
    // ===========================

    public DashboardAnalytics getDashboardAnalytics() {

        long totalProducts = productRepository.count();
        long totalStock = productRepository.getTotalStock();
        long lowStock = productRepository.getLowStockCount();
        long stockIn = transactionRepository.getTotalStockIn();
        long stockOut = transactionRepository.getTotalStockOut();

        return new DashboardAnalytics(
                totalProducts,
                totalStock,
                lowStock,
                stockIn,
                stockOut
        );
    }
}
