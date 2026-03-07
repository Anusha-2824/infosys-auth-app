package com.inventra.controller;

import com.inventra.dto.DashboardAnalytics;
import com.inventra.model.InventoryTransaction;
import com.inventra.model.Product;
import com.inventra.service.ProductService;
import com.inventra.repository.InventoryTransactionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final InventoryTransactionRepository transactionRepository;

    // ===========================
    // BASIC CRUD
    // ===========================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // ===========================
    // STOCK IN
    // ===========================

    @PostMapping("/{id}/stock-in")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<String> stockIn(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String note,
            Authentication authentication) {

        String username = authentication.getName();

        String response = productService.stockIn(
                id,
                quantity,
                username,
                note
        );

        return ResponseEntity.ok(response);
    }

    // ===========================
    // STOCK OUT
    // ===========================

    @PostMapping("/{id}/stock-out")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<String> stockOut(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String note,
            Authentication authentication) {

        String username = authentication.getName();

        String response = productService.stockOut(
                id,
                quantity,
                username,
                note
        );

        return ResponseEntity.ok(response);
    }

    // ===========================
    // LOW STOCK PRODUCTS
    // ===========================

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        return ResponseEntity.ok(productService.getLowStockProducts());
    }

    // ===========================
    // DASHBOARD ANALYTICS
    // ===========================

    @GetMapping("/analytics")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<DashboardAnalytics> getAnalytics() {
        return ResponseEntity.ok(productService.getDashboardAnalytics());
    }

    // ===========================
    // TRANSACTION HISTORY
    // ===========================

    @GetMapping("/transactions")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<InventoryTransaction>> getTransactions() {
        return ResponseEntity.ok(
                transactionRepository.findAllByOrderByIdDesc()
        );
    }
}
