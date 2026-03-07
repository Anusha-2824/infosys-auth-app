package com.inventra.repository;

import com.inventra.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ================= BASIC SEARCH =================

    List<Product> findByName(String name);

    List<Product> findByUnitPriceGreaterThan(Double unitPrice);

    List<Product> findByStockQuantityLessThan(Integer stockQuantity);

    Optional<Product> findBySku(String sku);


    // ================= DASHBOARD ANALYTICS =================

    @Query("SELECT COALESCE(SUM(p.stockQuantity), 0) FROM Product p")
    Long getTotalStock();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.stockQuantity < p.minStockLevel")
    Long getLowStockCount();

}
