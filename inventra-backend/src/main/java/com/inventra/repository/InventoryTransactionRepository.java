package com.inventra.repository;

import com.inventra.model.InventoryTransaction;
import com.inventra.model.TransactionType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryTransactionRepository
        extends JpaRepository<InventoryTransaction, Long> {

    // ================= BASIC HISTORY =================

    List<InventoryTransaction> findByProductId(Long productId);

    List<InventoryTransaction> findAllByOrderByIdDesc();


    // ================= DASHBOARD ANALYTICS =================

    @Query("SELECT COALESCE(SUM(t.quantity), 0) FROM InventoryTransaction t WHERE t.type = 'STOCK_IN'")
    Long getTotalStockIn();

    @Query("SELECT COALESCE(SUM(t.quantity), 0) FROM InventoryTransaction t WHERE t.type = 'STOCK_OUT'")
    Long getTotalStockOut();


    // ================= FILTER BY TYPE =================

    List<InventoryTransaction> findByType(TransactionType type);

}
